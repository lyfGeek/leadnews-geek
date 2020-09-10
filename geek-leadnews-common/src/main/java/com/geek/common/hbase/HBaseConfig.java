//package com.geek.common.hbase;
//
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//
///**
// * HBase 配置类，读取 HBase.properties 配置文件。
// */
//@Setter
//@Getter
//@Configuration
//@PropertySource("classpath:HBase.properties")
//public class HBaseConfig {
//
//    /**
//     * HBase 注册地址。
//     */
//    @Value("${HBase.zookeeper.quorum}")
//    private String zookeeper_quorum;
//    /**
//     * 超时时间。
//     */
//    @Value("${HBase.client.keyValue.maxsize}")
//    private String maxsize;
//
//    /**
//     * 创建HBaseClient。
//     *
//     * @return
//     */
//    @Bean
//    public com.geek.common.HBase.HBaseClient getHBaseClient() {
//        org.apache.hadoop.conf.Configuration HBaseConfiguration = getHBaseConfiguration();
//        return new com.geek.common.HBase.HBaseClient(HBaseConfiguration);
//    }
//
//    /**
//     * 获取 HBaseConfiguration 对象。
//     *
//     * @return
//     */
//    private org.apache.hadoop.conf.Configuration getHBaseConfiguration() {
//        org.apache.hadoop.conf.Configuration HBaseConfiguration = new org.apache.hadoop.conf.Configuration();
//        HBaseConfiguration.set("HBase.zookeeper.quorum", zookeeper_quorum);
//        HBaseConfiguration.set("HBase.client.keyValue.maxsize", maxsize);
//        return HBaseConfiguration;
//    }
//
//}
