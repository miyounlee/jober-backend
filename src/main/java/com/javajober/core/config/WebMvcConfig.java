package com.javajober.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final FileDirectoryConfig fileDirectoryConfig;

    public WebMvcConfig(FileDirectoryConfig fileDirectoryConfig) {
        this.fileDirectoryConfig = fileDirectoryConfig;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/files/**")
                .addResourceLocations("file:" + fileDirectoryConfig.getDirectoryPath());
    }
}
