package com.example.cbrmonitoringspring.integration;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.Decoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.lang.reflect.Type;

@Configuration
public class CbrFeignConfig {

    @Bean
    public Decoder feignDecoder(ObjectMapper objectMapper) {
        return new Decoder() {
            @Override
            public Object decode(Response response, Type type) throws IOException {
                if (response.body() == null) {
                    return null;
                }

                byte[] bodyBytes = StreamUtils.copyToByteArray(response.body().asInputStream());
                if (bodyBytes.length == 0) {
                    return null;
                }

                JavaType javaType = objectMapper.getTypeFactory().constructType(type);
                return objectMapper.readValue(bodyBytes, javaType);
            }
        };
    }
}
