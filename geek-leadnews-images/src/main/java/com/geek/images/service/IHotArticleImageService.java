package com.geek.images.service;

import com.geek.common.kafka.messages.app.ApHotArticleMessage;

public interface IHotArticleImageService {

    /**
     * 处理热点文章图片。
     *
     * @param message
     */
    void handleHotImage(ApHotArticleMessage message);

}
