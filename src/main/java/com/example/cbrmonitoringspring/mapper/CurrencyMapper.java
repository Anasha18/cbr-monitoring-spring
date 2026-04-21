package com.example.cbrmonitoringspring.mapper;

import com.example.cbrmonitoringspring.domain.Currency;
import com.example.cbrmonitoringspring.integration.dto.CurrencyResponse;


public class CurrencyMapper {

    public static Currency toDomain(CurrencyResponse.CBRCurrencyData dto) {
        return Currency.builder()
                .code(dto.charCode())
                .name(dto.name())
                .value(dto.value())
                .build();
    }
}
