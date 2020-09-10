package com.geek.images.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geek.common.kafka.messages.app.ApHotArticleMessage;
import com.geek.images.config.InitConfig;
import com.geek.images.service.ICacheImageService;
import com.geek.images.service.IHotArticleImageService;
import com.geek.model.article.pojos.ApArticle;
import com.geek.model.article.pojos.ApArticleContent;
import com.geek.model.article.pojos.ApHotArticles;
import com.geek.model.mappers.app.IApArticleContentMapper;
import com.geek.model.mappers.app.IApArticleMapper;
import com.geek.utils.common.ZipUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class HotArticleImageServiceImpl implements IHotArticleImageService {

    @Autowired
    private IApArticleContentMapper apArticleContentMapper;

    @Autowired
    private IApArticleMapper apArticleMapper;

    @Autowired
    private ICacheImageService cacheImageService;

    @Override
    public void handleHotImage(ApHotArticleMessage message) {
        ApHotArticles hotArticles = message.getData();
        log.info("处理热文章图片开始 #articleId：{}，message：{}", hotArticles.getArticleId(), JSON.toJSONString(hotArticles));

        // 内容中的图片进行缓存。
        ApArticleContent apArticleContent = apArticleContentMapper.selectByArticleId(hotArticles.getArticleId());
        if (null != apArticleContent) {
            String content = ZipUtils.gunzip(apArticleContent.getContent());
            JSONArray array = JSONArray.parseArray(content);
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = array.getJSONObject(i);
                if (!"image".equals(obj.getString("type"))) {
                    continue;
                }
                String imgUrl = obj.getString("value");
                // 不是 fastdfs 中的图片不进行缓存。
                if (!imgUrl.startsWith(InitConfig.PREFIX)) {
                    log.info("非站内图片不进行缓存#articleId：{}", hotArticles.getArticleId());
                    continue;
                }
                // 缓存图片。
                cacheImageService.cache2Redis(imgUrl, true);
            }
        }
        // 封面图片进行缓存。
        ApArticle apArticle = apArticleMapper.selectById(Long.valueOf(hotArticles.getArticleId()));
        if (apArticle != null && StringUtils.isNotEmpty(apArticle.getImages())) {
            String[] articleImages = apArticle.getImages().split(",");
            for (String img : articleImages) {
                if (!img.startsWith(InitConfig.PREFIX)) {
                    log.info("非站内图片不进行缓存#articleId：{}", hotArticles.getArticleId());
                    continue;
                }
                // 缓存图片。
                cacheImageService.cache2Redis(img, true);
            }
        }
        log.info("处理热文章图片结束#message：{}", JSON.toJSONString(message));
    }

}
