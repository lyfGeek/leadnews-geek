package com.geek.migration.entity;

import com.geek.common.HBase.entity.HBaseStorage;
import com.geek.common.HBase.entity.IHBaseInvoke;
import com.geek.common.common.storage.StorageData;
import com.geek.model.article.pojos.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Article 封装数据的工具类。
 */
@Setter
@Getter
public class ArticleQuantity {

    /**
     * 文章关系数据实体。
     */
    private ApArticle apArticle;
    /**
     * 文章配置实体。
     */
    private ApArticleConfig apArticleConfig;
    /**
     * 文章内容实体。
     */
    private ApArticleContent apArticleContent;
    /**
     * 文章作者实体。
     */
    private ApAuthor apAuthor;
    /**
     * 回调接口。
     */
    private IHBaseInvoke HBaseInvoke;

    public Integer getApArticleId() {
        if (null != apArticle) {
            return apArticle.getId();
        }
        return null;
    }

    /**
     * 将 ArticleQuantity 对象转换为 HBaseStorage 对象。
     *
     * @return
     */
    public HBaseStorage getHBaseStorage() {
        HBaseStorage HBaseStorage = new HBaseStorage();
        HBaseStorage.setRowKey(String.valueOf(apArticle.getId()));
        HBaseStorage.setHBaseInvoke(HBaseInvoke);
        StorageData apArticleData = StorageData.getStorageData(apArticle);
        if (null != apArticleData) {
            HBaseStorage.addStorageData(apArticleData);
        }

        StorageData apArticleConfigData = StorageData.getStorageData(apArticleConfig);
        if (null != apArticleConfigData) {
            HBaseStorage.addStorageData(apArticleConfigData);
        }

        StorageData apArticleContentData = StorageData.getStorageData(apArticleContent);
        if (null != apArticleContentData) {
            HBaseStorage.addStorageData(apArticleContentData);
        }

        StorageData apAuthorData = StorageData.getStorageData(apAuthor);
        if (null != apAuthorData) {
            HBaseStorage.addStorageData(apAuthorData);
        }
        return HBaseStorage;
    }


    /**
     * 获取 StorageData 列表。
     *
     * @return
     */
    public List<StorageData> getStorageDataList() {
        List<StorageData> storageDataList = new ArrayList<StorageData>();
        StorageData apArticleStorageData = StorageData.getStorageData(apArticle);
        if (null != apArticleStorageData) {
            storageDataList.add(apArticleStorageData);
        }

        StorageData apArticleContentStorageData = StorageData.getStorageData(apArticleContent);
        if (null != apArticleContentStorageData) {
            storageDataList.add(apArticleContentStorageData);
        }


        StorageData apArticleConfigStorageData = StorageData.getStorageData(apArticleConfig);
        if (null != apArticleConfigStorageData) {
            storageDataList.add(apArticleConfigStorageData);
        }

        StorageData apAuthorStorageData = StorageData.getStorageData(apAuthor);
        if (null != apAuthorStorageData) {
            storageDataList.add(apAuthorStorageData);
        }
        return storageDataList;
    }

    public ApHotArticles getApHotArticles() {
        ApHotArticles apHotArticles = null;
        if (null != apArticle) {
            apHotArticles = new ApHotArticles();
            apHotArticles.setArticleId(apArticle.getId());
            apHotArticles.setReleaseDate(apArticle.getPublishTime());
            apHotArticles.setScore(1);
            // apHotArticles.setTagId();
            apHotArticles.setTagName(apArticle.getLabels());
            apHotArticles.setCreatedTime(new Date());
        }
        return apHotArticles;
    }

}
