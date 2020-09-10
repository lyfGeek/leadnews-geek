package com.geek.common.kafka.messages.behavior;

import com.geek.common.kafka.KafkaMessage;
import com.geek.model.behavior.pojos.ApReadBehavior;

public class UserReadMessage extends KafkaMessage<ApReadBehavior> {

    public UserReadMessage() {
    }

    public UserReadMessage(ApReadBehavior data) {
        super(data);
    }

    @Override
    public String getType() {
        return "user-read";
    }

}
