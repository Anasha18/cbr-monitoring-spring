package com.example.cbrmonitoringspring.integration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

public record CurrencyResponse(
        @JsonProperty("Date")
        String date,

        @JsonProperty("PreviousDate")
        String previousDate,

        @JsonProperty("PreviousURL")
        String previousURL,

        @JsonProperty("Timestamp")
        String timestamp,

        @JsonProperty("Valute")
        Map<String, CBRCurrencyData> valute
) {
    public record CBRCurrencyData(
            @JsonProperty("ID")
            String id,

            @JsonProperty("NumCode")
            String numCode,

            @JsonProperty("CharCode")
            String charCode,

            @JsonProperty("Nominal")
            Integer nominal,

            @JsonProperty("Name")
            String name,

            @JsonProperty("Value")
            BigDecimal value,

            @JsonProperty("Previous")
            BigDecimal previous
    ) {
    }
}