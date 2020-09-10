package com.geek.migration.service;

import com.geek.migration.entity.ArticleQuantity;

import java.util.List;

public interface IArticleQuantityService {

    /**
     * 获取 ArticleQuantity 列表。
     *
     * @return
     */
    List<ArticleQuantity> getArticleQuantityList();

    /**
     * 根据文章 id 查询 ArticleQuantity。
     *
     * @param id
     * @return
     */
    ArticleQuantity getArticleQuantityByArticleId(Long id);

    /**
     * 根据文章 id 从 HBase 中查询 ArticleQuantity。
     *
     * @param id
     * @return
     */
    ArticleQuantity getArticleQuantityByArticleIdForHBase(Long id);

    /**
     * 数据库同步到 HBase。
     */
    void dbToHBase();

    /**
     * 根据文章 id 将数据库的数据同步到 HBase。
     *
     * @param articleId
     */
    void dbToHBase(Integer articleId);

}
