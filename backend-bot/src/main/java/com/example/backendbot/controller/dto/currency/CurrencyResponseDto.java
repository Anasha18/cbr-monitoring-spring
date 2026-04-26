package com.example.backendbot.controller.dto.currency;

import java.math.BigDecimal;

public record CurrencyResponseDto(
        Long id,
        String code,
        String name,
        BigDecimal value
) {
}
