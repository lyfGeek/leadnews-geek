package com.geek.common.kafka.messages;

import com.geek.common.kafka.KafkaMessage;
import com.geek.model.mess.app.UpdateArticle;

public class UpdateArticleMessage extends KafkaMessage<UpdateArticle> {

    public UpdateArticleMessage() {
    }

    public UpdateArticleMessage(UpdateArticle data) {
        super(data);
    }

    @Override
    public String getType() {
        return "update-article";
    }

}
