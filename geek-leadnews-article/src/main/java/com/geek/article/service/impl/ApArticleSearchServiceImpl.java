package com.geek.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.geek.article.service.IApArticleSearchService;
import com.geek.article.utils.Trie;
import com.geek.common.common.constant.ESIndexConstants;
import com.geek.model.article.dtos.UserSearchDto;
import com.geek.model.article.pojos.ApArticle;
import com.geek.model.article.pojos.ApAssociateWords;
import com.geek.model.article.pojos.ApHotWords;
import com.geek.model.behavior.pojos.ApBehaviorEntry;
import com.geek.model.common.dtos.ResponseResult;
import com.geek.model.common.enums.AppHttpCodeEnum;
import com.geek.model.mappers.app.*;
import com.geek.model.user.pojos.ApUser;
import com.geek.model.user.pojos.ApUserSearch;
import com.geek.utils.threadlocal.AppThreadLocalUtils;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApArticleSearchServiceImpl implements IApArticleSearchService {

    @Autowired
    private IApBehaviorEntryMapper apBehaviorEntryMapper;
    @Autowired
    private IApUserSearchMapper apUserSearchMapper;
    @Autowired
    private IApHotWordsMapper apHotWordsMapper;
    @Autowired
    private IApAssociateWordsMapper apAssociateWordsMapper;
    @Autowired
    private JestClient jestClient;
    @Autowired
    private IApArticleMapper apArticleMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    public ResponseResult getEntryId(UserSearchDto dto) {
        ApUser user = AppThreadLocalUtils.getUser();
        // 用户和设备不能同时为空。
        if (user == null && dto.getEquipmentId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        Long userId = null;
        if (user != null) {
            userId = user.getId();
        }
        ApBehaviorEntry apBehaviorEntry = apBehaviorEntryMapper.selectByUserIdOrEquipmentId(userId, dto.getEquipmentId());
        // 行为实体找以及注册了，逻辑上这里是必定有值得，除非参数错误。
        if (apBehaviorEntry == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        return ResponseResult.okResult(apBehaviorEntry.getId());
    }

    @Override
    public ResponseResult findUserSearch(UserSearchDto dto) {
        if (dto.getPageSize() > 50 || dto.getPageSize() < 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 先获取行为实体 id。
        ResponseResult result = getEntryId(dto);
        if (result.getCode() != AppHttpCodeEnum.SUCCESS.getCode()) {
            return result;
        }
        // 查询搜索记录。
        List<ApUserSearch> list = apUserSearchMapper.selectByEntryId((int) result.getData(), dto.getPageSize());
        return ResponseResult.okResult(list);
    }

    @Override
    public ResponseResult delUserSearch(UserSearchDto dto) {
        if (dto.getHisList() == null || dto.getHisList().size() <= 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        // 获取行为实体id。
        ResponseResult result = getEntryId(dto);
        if (result.getCode() != AppHttpCodeEnum.SUCCESS.getCode()) {
            return result;
        }
        List<Integer> ids = dto.getHisList().stream().map(ApUserSearch::getId).collect(Collectors.toList());
        // 通过 id 修改状态。
        int count = apUserSearchMapper.delUserSearch((int) result.getData(), ids);
        return ResponseResult.okResult(count);
    }

    @Override
    public ResponseResult clearUserSearch(UserSearchDto dto) {
        ResponseResult result = getEntryId(dto);
        if (result.getCode() != AppHttpCodeEnum.SUCCESS.getCode()) {
            return result;
        }
        int count = apUserSearchMapper.clearUserSearch((int) result.getData());
        return ResponseResult.okResult(count);
    }

    @Override
    public ResponseResult hotkeywords(String date) {
        if (StringUtils.isEmpty(date)) {
            date = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        }
        List<ApHotWords> apHotWords = apHotWordsMapper.queryByHotDate(date);
        return ResponseResult.okResult(apHotWords);
    }

    @Override
    public ResponseResult searchAssociate(UserSearchDto dto) {
        if (dto.getPageSize() > 50 || dto.getPageSize() < 1) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        List<ApAssociateWords> apAssociateWords = apAssociateWordsMapper.selectByAssociateWords("%" + dto.getSearchWords() + "%", dto.getPageSize());
        return ResponseResult.okResult(apAssociateWords);
    }

    @Override
    public ResponseResult saveUserSearch(Integer entryId, String searchWords) {
        // 去检查当前数据是否存在。
        int count = apUserSearchMapper.checkExist(entryId, searchWords);
        if (count > 0) {
            return ResponseResult.okResult(1);
        }
        // 不存在，保存。
        ApUserSearch apUserSearch = new ApUserSearch();
        apUserSearch.setEntryId(entryId);
        apUserSearch.setKeyword(searchWords);
        apUserSearch.setStatus(1);
        apUserSearch.setCreatedTime(new Date());
        int insert = apUserSearchMapper.insert(apUserSearch);
        return ResponseResult.okResult(insert);
    }

    @Override
    public ResponseResult esArticleSearch(UserSearchDto dto) {
        // 保存搜索记录，只在第一页查询的时候进行保存。
        if (dto.getFromIndex() == 0) {
            ResponseResult ret = getEntryId(dto);
            if (ret.getCode() != AppHttpCodeEnum.SUCCESS.getCode()) {
                return ret;
            }
            saveUserSearch((int) ret.getData(), dto.getSearchWords());
        }
        // 构建查询条件 jestClient。
        // 按照关键字查询，分页查询。
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("title", dto.getSearchWords()));
        searchSourceBuilder.from(dto.getFromIndex());
        searchSourceBuilder.size(dto.getPageSize());
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(ESIndexConstants.ARTICLE_INDEX)
                .addType(ESIndexConstants.DEFAULT_DOC).build();
        try {
            SearchResult searchResult = jestClient.execute(search);
            List<ApArticle> apArticles = searchResult.getSourceAsObjectList(ApArticle.class);
            List<ApArticle> resultList = new ArrayList<>();
            for (ApArticle apArticle : apArticles) {
                apArticle = apArticleMapper.selectById(Long.valueOf(apArticle.getId()));
                if (apArticle == null) {
                    continue;
                }
                resultList.add(apArticle);
            }
            // 获取结果。
            return ResponseResult.okResult(resultList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
    }

    @Override
    public ResponseResult searchAssociationV2(UserSearchDto dto) {
        if (dto == null || dto.getPageSize() > 50) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        String assoStr = redisTemplate.opsForValue().get("associte_list");
        List<ApAssociateWords> aw = null;
        if (StringUtils.isNotEmpty(assoStr)) {
            aw = JSON.parseArray(assoStr, ApAssociateWords.class);
        } else {
            aw = apAssociateWordsMapper.selectAllAssociateWords();
            redisTemplate.opsForValue().set("associte_list", JSON.toJSONString(aw));
        }

        // 使用 trie 来进行管理数据。
        Trie t = new Trie();
        for (ApAssociateWords a : aw) {
            t.insert(a.getAssociateWords());
        }
        List<String> ret = t.startWith(dto.getSearchWords());
        List<ApAssociateWords> list = new ArrayList<>();
        for (String s : ret) {
            ApAssociateWords apAssociateWords = new ApAssociateWords();
            apAssociateWords.setAssociateWords(s);
            list.add(apAssociateWords);
        }
        return ResponseResult.okResult(list);
    }

}
