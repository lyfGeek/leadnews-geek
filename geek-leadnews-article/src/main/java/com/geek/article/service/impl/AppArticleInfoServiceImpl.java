package com.geek.article.service.impl;

import com.geek.article.service.IAppArticleInfoService;
import com.geek.model.article.dtos.ArticleInfoDto;
import com.geek.model.article.pojos.ApArticleConfig;
import com.geek.model.article.pojos.ApArticleContent;
import com.geek.model.article.pojos.ApAuthor;
import com.geek.model.article.pojos.ApCollection;
import com.geek.model.behavior.pojos.ApBehaviorEntry;
import com.geek.model.behavior.pojos.ApLikesBehavior;
import com.geek.model.behavior.pojos.ApUnlikesBehavior;
import com.geek.model.common.dtos.ResponseResult;
import com.geek.model.common.enums.AppHttpCodeEnum;
import com.geek.model.mappers.app.*;
import com.geek.model.user.pojos.ApUser;
import com.geek.model.user.pojos.ApUserFollow;
import com.geek.utils.common.BurstUtils;
import com.geek.utils.common.ZipUtils;
import com.geek.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AppArticleInfoServiceImpl implements IAppArticleInfoService {

    @Autowired
    private IApArticleConfigMapper apArticleConfigMapper;
    @Autowired
    private IApArticleContentMapper apArticleContentMapper;
    @Autowired
    private IApBehaviorEntryMapper apBehaviorEntryMapper;
    @Autowired
    private IApCollectionMapper apCollectionMapper;
    @Autowired
    private IApLikesBehaviorMapper apLikesBehaviorMapper;
    @Autowired
    private IApUnlikesBehaviorMapper apUnlikesBehaviorMapper;
    @Autowired
    private IApAuthorMapper apAuthorMapper;
    @Autowired
    private IApUserFollowMapper apUserFollowMapper;

    /**
     * 加载文章详情。
     */
    @Override
    public ResponseResult getArticleInfo(Integer articleId) {

        if (articleId == null || articleId < 1) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        Map<String, Object> dataMap = new HashMap<>();

        // 根据文章 id 查询 config 信息。
        ApArticleConfig apArticleConfig = apArticleConfigMapper.selectByArticleId(articleId);
        // 判断当前文章是否删除。
        if (apArticleConfig == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        } else if (!apArticleConfig.getIsDelete()) {// 没有删除。
            ApArticleContent apArticleContent = apArticleContentMapper.selectByArticleId(articleId);
            String content = ZipUtils.gunzip(apArticleContent.getContent());
            apArticleContent.setContent(content);
            dataMap.put("content", apArticleContent);
        }
        dataMap.put("config", apArticleConfig);

        return ResponseResult.okResult(dataMap);
    }

    /**
     * 加载文章详情的初始化配置信息，比如关注，收藏，点赞，不喜欢。
     */
    @Override
    public ResponseResult loadArticleBehavior(ArticleInfoDto dto) {

        return null;

//        ApUser user = AppThreadLocalUtils.getUser();
//        // 用户与设备不能同时为空。
//        if (user == null && dto.getEquipmentId() == null) {
//            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
//        }
//        // 通过 equipmentId 或用户 id 查看行为实体 ---> entryId。
//        Long userId = null;
//        if (user != null) {
//            userId = user.getId();
//        }
//        ApBehaviorEntry apBehaviorEntry = apBehaviorEntryMapper.selectByUserIdOrEquipmentId(userId, dto.getEquipmentId());
//
//        boolean isUnlike = false, isLike = false, isCollection = false, isFollow = false;
//        String burst = BurstUtils.groudOne(apBehaviorEntry.getId());
//
//        // 通过 entryId 和 articleId 去查看收藏表，看是否有数据，有数据就说明已经收藏，否则就是没有收藏。
//        ApCollection apCollection = apCollectionMapper.selectForEntryId(burst, apBehaviorEntry.getId(), dto.getArticleId(), ApCollection.Type.ARTICLE.getCode());
//        if (apCollection != null) {
//            isCollection = true;
//        }
//        // 通过 entryId 和 articleId 去查看点赞表，看是否有数据，有数据就说明已经点赞，否则就是没有点赞。
//        ApLikesBehavior apLikesBehavior = apLikesBehaviorMapper.selectLastLike(burst, apBehaviorEntry.getId(), dto.getArticleId(), ApCollection.Type.ARTICLE.getCode());
//        if (apLikesBehavior != null && apLikesBehavior.getOperation() == ApLikesBehavior.Operation.LIKE.getCode()) {
//            isLike = true;
//        }
//        // 通过 entryId 和 articleId 去查看不喜欢表，看是否有数据，有数据就说明不喜欢，否则就是喜欢。
//        ApUnlikesBehavior apUnlikesBehavior = apUnlikesBehaviorMapper.selectLastUnLike(apBehaviorEntry.getId(), dto.getArticleId());
//        if (apUnlikesBehavior != null && apUnlikesBehavior.getType() == ApUnlikesBehavior.Type.UNLIKE.getCode()) {
//            isUnlike = true;
//        }
//
//        // 通过 authorId 去 app 的 id ~ userId（app 账号信息 id）。
//        ApAuthor apAuthor = apAuthorMapper.selectById(dto.getAuthorId());
//        // 查看关注表，根据当前登录人的 userId 与作者的 app 账号 id 去查询，如果有数据，就说明已经关注，没有则说明没有关注。
//        if (user != null && apAuthor != null && apAuthor.getUserId() != null) {
//            ApUserFollow apUserFollow = apUserFollowMapper.selectByFollowId(BurstUtils.groudOne(user.getId()), user.getId(), apAuthor.getUserId().intValue());
//            if (apUserFollow != null) {
//                isFollow = true;
//            }
//        }
//        Map<String, Object> dataMap = new HashMap<>();
//        dataMap.put("isfollow", isFollow);
//        dataMap.put("islike", isLike);
//        dataMap.put("isunlike", isUnlike);
//        dataMap.put("iscollection", isCollection);
//
//        return ResponseResult.okResult(dataMap);
    }

}
