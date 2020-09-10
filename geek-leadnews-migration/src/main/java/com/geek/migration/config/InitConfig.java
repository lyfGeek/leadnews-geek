package com.geek.migration.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan({"com.geek.common.common.init", "com.geek.common.mongo", "com.geek.common.mysql.core", "com.geek.common.quartz", "com.geek.common.HBase", "com.geek.common.kafka"})
@EnableScheduling
public class InitConfig {
}
