package com.uade.seminario.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Instagram API
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "instagram.api")
public class InstagramConfig {
    
    private String baseUrl;
    private String accessToken;
    private String verifyToken;
    private String pageId;
}

