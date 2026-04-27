package com.example.backendbot.integration.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import feign.codec.Decoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StreamUtils;

@Configuration
public class CbrFeignConfig {

    @Bean
    public Decoder feignDecoder(ObjectMapper objectMapper) {
        return (response, type) -> {
            if (response.body() == null) {
                return null;
            }

            byte[] bodyBytes = StreamUtils.copyToByteArray(response.body().asInputStream());
            if (bodyBytes.length == 0) {
                return null;
            }

            JavaType javaType = objectMapper.getTypeFactory().constructType(type);
            return objectMapper.readValue(bodyBytes, javaType);
        };
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Logger feignLogger() {
        return new SimpleFeignLogger();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
