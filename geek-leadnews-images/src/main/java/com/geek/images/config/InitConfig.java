package com.geek.images.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.geek.common.common.init"})
public class InitConfig {

    public static String PREFIX = "http://192.168.25.133";

}
