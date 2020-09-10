package com.geek.media.config;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ServletComponentScan("com.geek.common.web.wm.security")
public class SecurityConfig {
}
