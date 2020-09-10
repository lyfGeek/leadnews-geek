package com.geek.migration.service;

import com.geek.model.article.pojos.ApArticle;

import java.util.List;

public interface IApArticleService {

    ApArticle getById(Long id);

    /**
     * 查询未同步的数据。
     *
     * @return
     */
    List<ApArticle> getUnSyncArticleList();

    /**
     * 更新同步状态。
     *
     * @param apArticle
     */
    void updateSyncStatus(ApArticle apArticle);

}
