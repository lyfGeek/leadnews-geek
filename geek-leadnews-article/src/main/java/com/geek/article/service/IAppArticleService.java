package com.geek.article.service;

import com.geek.model.article.dtos.ArticleHomeDto;
import com.geek.model.common.dtos.ResponseResult;
import com.geek.model.mess.app.ArticleVisitStreamDto;

public interface IAppArticleService {

    /**
     * type。1 ~ 加载更多。2 ~ 加载更新。
     *
     * @param articleHomeDto
     * @param type
     * @return
     */
    ResponseResult load(ArticleHomeDto articleHomeDto, Short type);

    /**
     * 更新 点赞 阅读数。
     *
     * @param articleVisitStreamDto
     * @return
     */
    ResponseResult updateArticleView(ArticleVisitStreamDto articleVisitStreamDto);

    /**
     * 加载文章列表数据。
     *
     * @param type
     * @param articleHomeDto
     * @param firstPage
     * @return
     */
    ResponseResult loadV2(Short type, ArticleHomeDto articleHomeDto, boolean firstPage);

}
