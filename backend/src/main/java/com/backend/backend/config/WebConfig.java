package com.backend.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/topics/**")
                .addResourceLocations("file:upload/topics/");
        registry.addResourceHandler("/upload/covers/**")
                .addResourceLocations("file:upload/covers/");
        registry.addResourceHandler("/upload/avatars/**")
                .addResourceLocations("file:upload/avatars/");
    }
}
