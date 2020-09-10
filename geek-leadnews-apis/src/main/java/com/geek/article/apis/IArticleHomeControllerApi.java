package com.geek.article.apis;

import com.geek.model.article.dtos.ArticleHomeDto;
import com.geek.model.common.dtos.ResponseResult;

public interface IArticleHomeControllerApi {

    /**
     * 加载文章首页。
     *
     * @param articleHomeDto
     * @return
     */
    ResponseResult load(ArticleHomeDto articleHomeDto);

    /**
     * 加载更多。
     *
     * @param articleHomeDto
     * @return
     */
    ResponseResult loadMore(ArticleHomeDto articleHomeDto);

    /**
     * 加载最新。
     *
     * @param articleHomeDto
     * @return
     */
    ResponseResult loadNew(ArticleHomeDto articleHomeDto);
}
