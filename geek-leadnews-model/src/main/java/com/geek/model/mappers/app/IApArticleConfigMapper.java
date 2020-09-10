package com.geek.model.mappers.app;

import com.geek.model.article.pojos.ApArticleConfig;

import java.util.List;

public interface IApArticleConfigMapper {

    ApArticleConfig selectByArticleId(Integer articleId);

    int insert(ApArticleConfig apArticleConfig);

    List<ApArticleConfig> selectByArticleIds(List<String> articleIds);

}
