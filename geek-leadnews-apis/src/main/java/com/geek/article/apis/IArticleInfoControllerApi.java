package com.geek.article.apis;

import com.geek.model.article.dtos.ArticleInfoDto;
import com.geek.model.common.dtos.ResponseResult;

public interface IArticleInfoControllerApi {

    ResponseResult loadArticleInfo(ArticleInfoDto articleInfoDto);

    ResponseResult loadArticleBehavior(ArticleInfoDto dto);

}
