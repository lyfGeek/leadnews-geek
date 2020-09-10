package com.geek.migration.service.impl;

import com.geek.common.HBase.HBaseStorageClient;
import com.geek.common.HBase.constants.HBaseConstants;
import com.geek.common.HBase.entity.HBaseStorage;
import com.geek.migration.entity.ArticleHBaseInvok;
import com.geek.migration.entity.ArticleQuantity;
import com.geek.migration.service.*;
import com.geek.model.article.pojos.ApArticle;
import com.geek.model.article.pojos.ApArticleConfig;
import com.geek.model.article.pojos.ApArticleContent;
import com.geek.model.article.pojos.ApAuthor;
import com.geek.utils.common.DataConvertUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ArticleQuantityServiceImpl implements IArticleQuantityService {

    @Autowired
    private IApArticleConfigService apArticleConfigService;

    @Autowired
    private IApArticleContentService apArticleContentService;

    @Autowired
    private IApArticleService apArticleService;

    @Autowired
    private IApAuthorService apAuthorService;

    @Autowired
    private HBaseStorageClient storageClient;

    @Override
    public List<ArticleQuantity> getArticleQuantityList() {
        log.info("生成 ArticleQuantity 列表。");
        List<ApArticle> unSyncArticleList = apArticleService.getUnSyncArticleList();
        // 获取文章 id 列表
        List<String> apArticleIdList = unSyncArticleList.stream().map(apArticle -> String.valueOf(apArticle.getId())).collect(Collectors.toList());
        // 获取文章中的作者 id 列表。
        List<Integer> apAuthorIdList = unSyncArticleList.stream().map(apArticle -> apArticle.getAuthorId() == null ? null : apArticle.getAuthorId().intValue()).filter(x -> x != null).collect(Collectors.toList());
        // 根据文章 id 列表查询文章配置列表和文章内容列表。
        List<ApArticleConfig> apArticleConfigList = apArticleConfigService.queryByArticleIds(apArticleIdList);
        List<ApArticleContent> apArticleContentList = apArticleContentService.queryByArticleIds(apArticleIdList);
        // 根据作者 id 列表查询作者列表。
        List<ApAuthor> apAuthorList = apAuthorService.queryByIds(apAuthorIdList);
        // 综合 ArticleQuantity --- list。
        List<ArticleQuantity> articleQuantityList = unSyncArticleList.stream().map(apArticle -> new ArticleQuantity() {{
            setApArticle(apArticle);
            // 根据文章的 id 过滤出符合要求的内容对象。
            List<ApArticleContent> apArticleContents = apArticleContentList.stream().filter(x -> x.getArticleId().equals(apArticle.getId())).collect(Collectors.toList());
            if (!apArticleContents.isEmpty()) {
                setApArticleContent(apArticleContents.get(0));
            }
            List<ApArticleConfig> apArticleConfigs = apArticleConfigList.stream().filter(x -> x.getArticleId().equals(apArticle.getId())).collect(Collectors.toList());
            if (!apArticleConfigs.isEmpty()) {
                setApArticleConfig(apArticleConfigs.get(0));
            }
            List<ApAuthor> apAuthors = apAuthorList.stream().filter(x -> x.getId().equals(apArticle.getAuthorId().intValue())).collect(Collectors.toList());
            if (!apAuthors.isEmpty()) {
                setApAuthor(apAuthors.get(0));
            }
            // 设置回调方法，用户方法的回调，用于修改同步的状态，查询 HBase，成功后同步状态修改。
            setHBaseInvoke(new ArticleHBaseInvok(apArticle, (x) -> apArticleService.updateSyncStatus(x)));
        }}).collect(Collectors.toList());

        if (null != articleQuantityList && !articleQuantityList.isEmpty()) {
            log.info("生成articleQuantity列表完成。size：{}", articleQuantityList.size());
        } else {
            log.info("生成articleQuantity列表完成。size：{}", 0);
        }
        return articleQuantityList;
    }

    @Override
    public ArticleQuantity getArticleQuantityByArticleId(Long id) {
        if (null == id) {
            return null;
        }
        ArticleQuantity articleQuantity = null;
        ApArticle apArticle = apArticleService.getById(id);
        if (null != apArticle) {
            articleQuantity = new ArticleQuantity();
            articleQuantity.setApArticle(apArticle);
            ApArticleContent apArticleContent = apArticleContentService.getByArticleId(apArticle.getId());
            articleQuantity.setApArticleContent(apArticleContent);
            ApArticleConfig apArticleConfig = apArticleConfigService.getByArticleId(apArticle.getId());
            articleQuantity.setApArticleConfig(apArticleConfig);
            ApAuthor apAuthor = apAuthorService.getById(apArticle.getAuthorId());
            articleQuantity.setApAuthor(apAuthor);
        }
        return articleQuantity;
    }

    @Override
    public ArticleQuantity getArticleQuantityByArticleIdForHBase(Long id) {
        if (null == id) {
            return null;
        }
        ArticleQuantity articleQuantity = null;
        List<Class> typeList = Arrays.asList(ApArticle.class, ApArticleConfig.class, ApArticleContent.class, ApAuthor.class);
        List<Object> objectList = storageClient.getStorageDataEntityList(HBaseConstants.APARTICLE_QUANTITY_TABLE_NAME, DataConvertUtils.toString(id), typeList);
        if (null != objectList && !objectList.isEmpty()) {
            articleQuantity = new ArticleQuantity();
            for (Object value : objectList) {
                if (value instanceof ApArticle) {
                    articleQuantity.setApArticle((ApArticle) value);
                } else if (value instanceof ApArticleContent) {
                    articleQuantity.setApArticleContent((ApArticleContent) value);
                } else if (value instanceof ApArticleConfig) {
                    articleQuantity.setApArticleConfig((ApArticleConfig) value);
                } else if (value instanceof ApAuthor) {
                    articleQuantity.setApAuthor((ApAuthor) value);
                }
            }
        }
        return articleQuantity;
    }

    @Override
    public void dbToHBase() {
        long currentTimeMillis = System.currentTimeMillis();
        List<ArticleQuantity> articleQuantityList = getArticleQuantityList();
        if (null != articleQuantityList && !articleQuantityList.isEmpty()) {
            log.info("开始进行定时数据库到 HBase 同步，帅选出未同步的数据量：{}", articleQuantityList.size());
            List<HBaseStorage> HBaseStorageList = articleQuantityList.stream().map(ArticleQuantity::getHBaseStorage).collect(Collectors.toList());
            storageClient.addHBaseStorage(HBaseConstants.APARTICLE_QUANTITY_TABLE_NAME, HBaseStorageList);
        } else {
            log.info("定时数据库到 HBase 中，没有找到数据。");
        }
        log.info("定时数据库到 HBase 同步结束，耗时：{}", System.currentTimeMillis() - currentTimeMillis);

    }

    @Override
    public void dbToHBase(Integer articleId) {
        long currentTimeMillis = System.currentTimeMillis();
        log.info("开始进行异步数据库到 HBase 同步。articleId：{}", articleId);
        if (null != articleId) {
            ArticleQuantity articleQuantity = getArticleQuantityByArticleId(articleId.longValue());
            if (null != articleQuantity) {
                HBaseStorage HBaseStorage = articleQuantity.getHBaseStorage();
                storageClient.addHBaseStorage(HBaseConstants.APARTICLE_QUANTITY_TABLE_NAME, HBaseStorage);
            }
        }
        log.info("异步数据库到 HBase 同步完成。articleId：{}，耗时：{}", articleId, System.currentTimeMillis() - currentTimeMillis);
    }

}
