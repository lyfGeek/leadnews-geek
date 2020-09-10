package com.geek.common.kafka.messages.app;

import com.geek.common.kafka.KafkaMessage;
import com.geek.model.article.pojos.ApHotArticles;

public class ApHotArticleMessage extends KafkaMessage<ApHotArticles> {

    public ApHotArticleMessage() {
    }

    public ApHotArticleMessage(ApHotArticles data) {
        super(data);
    }

    @Override
    public String getType() {
        return "hot-article";
    }

}
