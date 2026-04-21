package com.example.cbrmonitoringspring.configuration;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Data
@ToString
@ConfigurationProperties("business-logic")
public class BusinessLogicProperties {

    @NestedConfigurationProperty
    TelegramConfig telegram;

    @NestedConfigurationProperty
    CbrApiConfig cbrApi;
}
