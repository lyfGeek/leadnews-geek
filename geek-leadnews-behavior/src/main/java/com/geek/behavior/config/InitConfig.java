package com.geek.behavior.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.geek.common.common.init", "com.geek.common.kafka"})
public class InitConfig {
}
