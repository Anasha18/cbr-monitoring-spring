package com.example.backendbot.mapper;


import com.example.backendbot.controller.dto.currency.CurrencyRequestDto;
import com.example.backendbot.controller.dto.currency.CurrencyResponseDto;
import com.example.backendbot.domain.Currency;
import com.example.backendbot.integration.dto.CurrencyResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    default Currency toDomain(CurrencyResponse.CBRCurrencyData dto) {
        return Currency.builder()
                .code(dto.charCode())
                .name(dto.name())
                .value(dto.value())
                .build();
    }

    CurrencyResponseDto toDto(Currency currency);

    Currency toDomain(CurrencyRequestDto dto);
}
