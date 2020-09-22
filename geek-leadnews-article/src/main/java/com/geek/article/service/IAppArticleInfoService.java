package com.geek.article.service;

import com.geek.model.article.dtos.ArticleInfoDto;
import com.geek.model.common.dtos.ResponseResult;

public interface IAppArticleInfoService {

    /**
     * 加载文章详情。
     */
    ResponseResult getArticleInfo(Integer articleId);

    /**
     * 加载文章详情的初始化配置信息，比如关注，收藏，点赞，不喜欢。
     */
    ResponseResult loadArticleBehavior(ArticleInfoDto dto);

}
