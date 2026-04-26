package com.example.cbrmonitoringspring.integration.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UserRequestDto(
        @NotBlank String username,
        @NotNull @Positive Long telegramId
) {
}
