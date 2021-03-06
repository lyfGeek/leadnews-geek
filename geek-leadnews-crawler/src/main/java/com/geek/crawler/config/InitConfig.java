package com.geek.crawler.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan({"com.geek.common.common.init", "com.geek.common.mysql.core",
        "com.geek.common.kafka", "com.geek.common.quartz"})
@EnableScheduling
public class InitConfig {
}
