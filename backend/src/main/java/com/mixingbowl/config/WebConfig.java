package com.mixingbowl.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AppConfig appConfig;
    private static final String[] ALLOWED_METHODS = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};
    private static final String[] ALLOWED_HEADERS = {"Authorization", "Content-Type", "X-Requested-With"};
    private static final String[] EXPOSED_HEADERS = {"Authorization"};
    private static final long MAX_AGE = 3600L;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(appConfig.getFrontendUrl())
                .allowedMethods(ALLOWED_METHODS)
                .exposedHeaders(EXPOSED_HEADERS)
                .allowCredentials(true)
                .maxAge(MAX_AGE);
    }
}
