package com.geek.common.kafka.messages.app;

import com.geek.common.kafka.KafkaMessage;
import com.geek.model.mess.app.ArticleVisitStreamDto;

public class ArticleVisitStreamMessage extends KafkaMessage<ArticleVisitStreamDto> {

    public ArticleVisitStreamMessage() {
    }

    public ArticleVisitStreamMessage(ArticleVisitStreamDto data) {
        super(data);
    }

    @Override
    public String getType() {
        return "article-visit-stream";
    }

}
