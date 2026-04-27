package com.example.cbrmonitoringspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableFeignClients
@EnableScheduling
public class CbrMonitoringSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(CbrMonitoringSpringApplication.class, args);
    }

}
