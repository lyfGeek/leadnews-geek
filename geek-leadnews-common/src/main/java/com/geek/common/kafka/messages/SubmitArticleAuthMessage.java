package com.geek.common.kafka.messages;

import com.geek.common.kafka.KafkaMessage;
import com.geek.model.mess.admin.SubmitArticleAuto;

public class SubmitArticleAuthMessage extends KafkaMessage<SubmitArticleAuto> {

    public SubmitArticleAuthMessage() {
    }

    public SubmitArticleAuthMessage(SubmitArticleAuto data) {
        super(data);
    }

    @Override
    public String getType() {
        return "submit-article-auth";
    }

}
