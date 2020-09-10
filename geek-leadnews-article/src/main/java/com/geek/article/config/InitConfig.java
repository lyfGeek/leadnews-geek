package com.geek.article.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan({"com.geek.common.mysql.core", "com.geek.common.common.init", "com.geek.common.quartz", "com.geek.common.kafka", "com.geek.common.kafkastream"})
@EnableScheduling
public class InitConfig {
}
