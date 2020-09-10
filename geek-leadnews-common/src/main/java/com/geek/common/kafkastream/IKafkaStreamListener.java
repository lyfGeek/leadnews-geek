package com.geek.common.kafkastream;

/**
 * 流数据的监听消费者实现的接口类，系统自动会通过。
 * KafkaStreamListenerFactory 类扫描项目中实现该接口的类。
 * 并注册为流数据的消费端。
 * <p>
 * 其中泛型可是 KStream 或 KTable。
 *
 * @param <T>
 */
public interface IKafkaStreamListener<T> {

    /**
     * 监听的类型。
     *
     * @return
     */
    String listenerTopic();

    /**
     * 处理结果发送的类。
     *
     * @return
     */
    String sendTopic();

    /**
     * 对象处理逻辑。
     *
     * @param stream
     * @return
     */
    T getService(T stream);

}
