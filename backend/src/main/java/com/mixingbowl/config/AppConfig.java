package com.mixingbowl.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AppConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public Long getJwtExpiration() {
        return jwtExpiration;
    }

    public String getFrontendUrl() {
        return frontendUrl;
    }
}
