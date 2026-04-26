package com.example.cbrmonitoringspring.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("telegram")
public record TelegramConfig(
    String token,
    String username
) {
}
