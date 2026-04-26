package com.example.backendbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableFeignClients
@EnableCaching
public class BackendBotApplication {

    static void main(String[] args) {
        SpringApplication.run(BackendBotApplication.class, args);
    }

}
