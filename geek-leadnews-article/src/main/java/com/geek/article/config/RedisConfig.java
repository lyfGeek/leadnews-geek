package com.geek.article.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.geek.common.redis")
public class RedisConfig {
}
