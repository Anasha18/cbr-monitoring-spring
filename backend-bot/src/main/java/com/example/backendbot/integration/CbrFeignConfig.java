package com.example.backendbot.integration;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
