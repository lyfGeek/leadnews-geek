package com.geek.model.mappers.app;

import com.geek.model.article.pojos.ApArticleContent;

import java.util.List;

public interface IApArticleContentMapper {

    ApArticleContent selectByArticleId(Integer articleId);

    void insert(ApArticleContent apArticleContent);

    List<ApArticleContent> selectByArticleIds(List<String> articleIds);

}
