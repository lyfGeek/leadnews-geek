package com.geek.migration.service;

import com.geek.model.article.pojos.ApArticleConfig;

import java.util.List;

public interface IApArticleConfigService {

    List<ApArticleConfig> queryByArticleIds(List<String> ids);

    ApArticleConfig getByArticleId(Integer id);

}
