package com.geek.login.config;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ServletComponentScan("com.geek.common.web.app.security")
public class SecurityConfig {
}
