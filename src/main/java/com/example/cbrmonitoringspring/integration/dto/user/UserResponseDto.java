package com.example.cbrmonitoringspring.integration.dto.user;

import java.time.LocalDateTime;

public record UserResponseDto(
        Long id,
        Long telegramId,
        String username,
        LocalDateTime createdAt
) {
}
