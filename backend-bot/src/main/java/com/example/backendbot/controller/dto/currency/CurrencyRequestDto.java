package com.example.backendbot.controller.dto.currency;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CurrencyRequestDto(
        @NotBlank
        String code,
        @NotBlank
        String name,
        @NotNull
        BigDecimal value
) {
}
