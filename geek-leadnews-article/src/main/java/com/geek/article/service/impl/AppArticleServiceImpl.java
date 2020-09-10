package com.geek.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.geek.article.service.IAppArticleService;
import com.geek.common.article.constans.ArticleConstant;
import com.geek.model.article.dtos.ArticleHomeDto;
import com.geek.model.article.pojos.ApArticle;
import com.geek.model.article.pojos.ApHotArticles;
import com.geek.model.behavior.pojos.ApBehaviorEntry;
import com.geek.model.common.dtos.ResponseResult;
import com.geek.model.mappers.app.IApArticleMapper;
import com.geek.model.mappers.app.IApBehaviorEntryMapper;
import com.geek.model.mappers.app.IApHotArticlesMapper;
import com.geek.model.mappers.app.IApUserArticleListMapper;
import com.geek.model.mess.app.ArticleVisitStreamDto;
import com.geek.model.user.pojos.ApUser;
import com.geek.model.user.pojos.ApUserArticleList;
import com.geek.utils.threadlocal.AppThreadLocalUtils;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class AppArticleServiceImpl implements IAppArticleService {

    private static final short MAX_PAGE_SIZE = 50;

    @Autowired
    private IApArticleMapper apArticleMapper;
    @Autowired
    private IApUserArticleListMapper apUserArticleListMapper;
    @Autowired
    private IApBehaviorEntryMapper apBehaviorEntryMapper;
    @Autowired
    private IApHotArticlesMapper apHotArticlesMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * type。1 ~ 加载更多。2 ~ 加载更新。
     *
     * @param articleHomeDto
     * @param type
     * @return
     */
    @Override
    public ResponseResult load(ArticleHomeDto articleHomeDto, Short type) {
        // 参数校验。
        if (articleHomeDto == null) {
            articleHomeDto = new ArticleHomeDto();
        }
        // 时间校验。
        if (articleHomeDto.getMaxBehotTime() == null) {
            articleHomeDto.setMaxBehotTime(new Date());
        }
        if (articleHomeDto.getMinBehotTime() == null) {
            articleHomeDto.setMinBehotTime(new Date());
        }

        // 分页参数的校验。
        Integer size = articleHomeDto.getSize();
        if (size == null || size <= 0) {
            size = 20;// 默认值。
        }
        size = Math.min(size, MAX_PAGE_SIZE);// 最大 50。
        articleHomeDto.setSize(size);

        // 文章频道参数校验。
        if (StringUtils.isEmpty(articleHomeDto.getTag())) {
            articleHomeDto.setTag(ArticleConstant.DEFAULT_TAG);
        }

        // 类型参数校验。
        if (!type.equals(ArticleConstant.LOADTYPE_LOAD_MORE) && !type.equals(ArticleConstant.LOADTYPE_LOAD_NEW)) {
            type = ArticleConstant.LOADTYPE_LOAD_MORE;
        }

        // 获取用户的信息。
        ApUser user = AppThreadLocalUtils.getUser();

        // 判断用户是否存在。
        if (user != null) {
            // 存在（已登录），加载推荐的文章。
            List<ApArticle> apArticleList = this.getUserArticle(user, articleHomeDto, type);
            return ResponseResult.okResult(apArticleList);
        } else {
            // 不存在（未登录），加载默认的文章。
            List<ApArticle> apArticles = this.getDefaultArticle(articleHomeDto, type);
            return ResponseResult.okResult(apArticles);
        }
    }

    @Override
    public ResponseResult updateArticleView(ArticleVisitStreamDto articleVisitStreamDto) {
        int rows = apArticleMapper.updateArticleView(articleVisitStreamDto.getArticleId(), articleVisitStreamDto.getView(), articleVisitStreamDto.getCollect(), articleVisitStreamDto.getComment(), articleVisitStreamDto.getLike());
        return ResponseResult.okResult(rows);
    }

    /**
     * 加载默认的文章信息。
     *
     * @param articleHomeDto
     * @param type
     * @return
     */
    private List<ApArticle> getDefaultArticle(ArticleHomeDto articleHomeDto, Short type) {
        return apArticleMapper.loadArticleListByLocation(articleHomeDto, type);
    }

    /**
     * 先从用户的推荐表中查找文章信息，如果没有再从默认文章信息获取数据。
     *
     * @param user
     * @param articleHomeDto
     * @param type
     * @return
     */
    private List<ApArticle> getUserArticle(ApUser user, ArticleHomeDto articleHomeDto, Short type) {
        // 推荐文章列表。
        List<ApUserArticleList> list = apUserArticleListMapper.loadArticleIdListByUser(user, articleHomeDto, type);
        if (!list.isEmpty()) {
            return apArticleMapper.loadArticleListByIdList(list);
        } else {
            return getDefaultArticle(articleHomeDto, type);
        }
    }

    @Override
    public ResponseResult loadV2(Short type, ArticleHomeDto articleHomeDto, boolean firstPage) {
        // 参数校验。
        if (articleHomeDto == null) {
            articleHomeDto = new ArticleHomeDto();
        }
        // 时间校验。
        if (articleHomeDto.getMaxBehotTime() == null) {
            articleHomeDto.setMaxBehotTime(new Date());
        }

        if (articleHomeDto.getMinBehotTime() == null) {
            articleHomeDto.setMinBehotTime(new Date());
        }

        // 分页参数的校验。
        Integer size = articleHomeDto.getSize();
        if (size == null || size <= 0) {
            size = 20;
        }
        size = Math.min(size, MAX_PAGE_SIZE);
        articleHomeDto.setSize(size);

        // 文章频道参数校验。
        if (StringUtils.isEmpty(articleHomeDto.getTag())) {
            articleHomeDto.setTag(ArticleConstant.DEFAULT_TAG);
        }

        // 类型参数校验。
        if (!type.equals(ArticleConstant.LOADTYPE_LOAD_MORE) && !type.equals(ArticleConstant.LOADTYPE_LOAD_NEW)) {
            type = ArticleConstant.LOADTYPE_LOAD_MORE;
        }

        // 获取用户的信息。
        ApUser user = AppThreadLocalUtils.getUser();

        // 是否是首页。是首页 ~ 从缓存中获取数据。不是首页 ~ 从数据库中获取数据。
        if (firstPage) {
            List<ApArticle> cacheList = getCacheArticleV2(articleHomeDto);
            if (cacheList.size() > 0) {
                log.info("使用缓存加载数据#tag：{}", articleHomeDto.getTag());
                return ResponseResult.okResult(cacheList);
            }
        }
        // 用户是否为空。
        // 为空加载默认的。
        // 不为空，先去找推荐的热文章，找不到推荐的，再去查找默认推荐的热文章。
        if (user != null) {
            List<ApArticle> userArticleList = getUserArticleV2(user, articleHomeDto, type);
            return ResponseResult.okResult(userArticleList);
        } else {
            List<ApArticle> defualtArticleList = getDefaultArticleV2(articleHomeDto, type);
            return ResponseResult.okResult(defualtArticleList);
        }
    }

    /**
     * 先从用户推荐的表中查找热文章，如果没有再从大文章列表查询数据。
     *
     * @param user
     * @param articleHomeDto
     * @param type
     * @return
     */
    private List<ApArticle> getUserArticleV2(ApUser user, ArticleHomeDto articleHomeDto, Short type) {
        // 用户和设备不能同时为空。
        if (user == null) {
            return Lists.newArrayList();
        }
        Long userId = user.getId();
        ApBehaviorEntry apBehaviorEntry = apBehaviorEntryMapper.selectByUserIdOrEquipmentId(userId, null);
        // 行为实体找以及注册了，逻辑上这里是必定有值得，除非参数错误。
        if (apBehaviorEntry == null) {
            return Lists.newArrayList();
        }
        Integer entryId = apBehaviorEntry.getId();
        // 如果没查到，查询全局热文章。
        if (entryId == null) {
            entryId = 0;
        }
        List<ApHotArticles> list = apHotArticlesMapper.loadArticleIdListByEntryId(entryId, articleHomeDto, type);
        // 默认从热文章里查找。
        if (!list.isEmpty()) {
            List<Integer> articleIdList = list.stream().map(p ->
                    p.getArticleId()).collect(Collectors.toList());
            List<ApArticle> apArticles = apArticleMapper.loadArticleListByIdListV2(articleIdList);
            return apArticles;
        } else {
            return getDefaultArticleV2(articleHomeDto, type);
        }
    }

    /**
     * 加载默认的热文章数据。
     *
     * @param articleHomeDto
     * @param type
     * @return
     */
    private List<ApArticle> getDefaultArticleV2(ArticleHomeDto articleHomeDto, Short type) {
        List<ApHotArticles> hotList = apHotArticlesMapper.loadHotListByLocation(articleHomeDto, type);
        List<ApArticle> articleList = Lists.newArrayList();
        if (null != hotList && !hotList.isEmpty()) {
            for (ApHotArticles hotArticles : hotList) {
                ApArticle apArticle = apArticleMapper.selectById(Long.valueOf(hotArticles.getArticleId()));
                articleList.add(apArticle);
            }
        }
        return articleList;
    }

    /**
     * 查询缓存首页文章数据。
     *
     * @param articleHomeDto
     * @return
     */
    private List<ApArticle> getCacheArticleV2(ArticleHomeDto articleHomeDto) {
        log.info("查询缓存热文章数据#tag：{}", articleHomeDto.getTag());
        String ret = redisTemplate.opsForValue().get(ArticleConstant.HOT_ARTICLE_FIRST_PAGE + articleHomeDto.getTag());
        if (StringUtils.isEmpty(ret)) {
            return Lists.newArrayList();
        }
        List<ApArticle> list = JSON.parseArray(ret, ApArticle.class);
        log.info("查询缓存文章热数据#tag：{}。size：{}", articleHomeDto.getTag(), list.size());
        return list;
    }

}
