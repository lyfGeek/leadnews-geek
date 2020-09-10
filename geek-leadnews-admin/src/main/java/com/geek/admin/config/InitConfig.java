package com.geek.admin.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan({"com.geek.common.mysql.core", "com.geek.common.common.init", "com.geek.common.quartz"})
@MapperScan("com.geek.admin.dao")
@EnableScheduling
public class InitConfig {
}
