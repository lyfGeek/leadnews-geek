package com.geek.common.zookeeper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "zk")// 配置文件前缀。
@PropertySource("classpath:zookeeper.properties")// 加载配置文件。
public class ZkConfig {

    private String host;
    private String sequencePath;

    @Bean
    public ZookeeperClient zookeeperClient() {
        return new ZookeeperClient(this.host, this.sequencePath);
    }

}
