package com.geek.admin.config;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ServletComponentScan("com.geek.common.web.admin.security")
public class SecurityConfig {
}
