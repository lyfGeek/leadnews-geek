package com.geek.migration.service;

import com.geek.model.article.pojos.ApArticleContent;

import java.util.List;

public interface IApArticleContentService {

    List<ApArticleContent> queryByArticleIds(List<String> ids);

    ApArticleContent getByArticleId(Integer id);

}
