package com.geek.migration.service;

import com.geek.model.article.pojos.ApHotArticles;

import java.util.List;

public interface IApHotArticleService {

    List<ApHotArticles> selectList(ApHotArticles apHotArticles);

    void insert(ApHotArticles apHotArticles);

    /**
     * 热数据 HBase 同步。
     *
     * @param articleId
     */
    void hotApArticleSync(Integer articleId);

    void deleteById(Integer id);

    /**
     * 查询过期的数据。
     *
     * @return
     */
    List<ApHotArticles> selectExpireMonth();

    void deleteHotData(ApHotArticles apHotArticles);

}
