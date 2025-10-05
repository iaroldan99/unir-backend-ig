package com.uade.seminario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuración de WebClient para llamadas HTTP a la API de Instagram
 */
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(InstagramConfig instagramConfig) {
        return WebClient.builder()
                .baseUrl(instagramConfig.getBaseUrl())
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}

