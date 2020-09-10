package com.geek.common.kafka.messages.behavior;

import com.geek.common.kafka.KafkaMessage;
import com.geek.model.behavior.pojos.ApLikesBehavior;

public class UserLikesMessage extends KafkaMessage<ApLikesBehavior> {

    public UserLikesMessage() {
    }

    public UserLikesMessage(ApLikesBehavior data) {
        super(data);
    }

    @Override
    public String getType() {
        return "user-likes";
    }

}
