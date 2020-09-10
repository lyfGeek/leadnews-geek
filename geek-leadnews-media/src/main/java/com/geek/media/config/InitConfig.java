package com.geek.media.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.geek.common.common.init", "com.geek.common.fastdfs"})
public class InitConfig {
}
