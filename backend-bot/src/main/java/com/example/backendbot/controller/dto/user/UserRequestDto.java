package com.example.backendbot.controller.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UserRequestDto(
        String username,
        @NotNull @Positive Long telegramId
) {
}
