package com.geek.common.kafka;

import org.springframework.kafka.listener.ConsumerAwareMessageListener;

/**
 * 消息监听实现接口。
 */
public interface IKafkaListener<K, V> extends ConsumerAwareMessageListener<K, V> {

    String topic();

    default String factory() {
        return "defaultKafkaListenerContainerFactory";
    }

    default String group() {
        return "default";
    }

}
