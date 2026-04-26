package com.example.backendbot.controller.dto.subscription;

import java.time.LocalDateTime;

public record SubscriptionResponseDto(
        Long id,
        Boolean isActive,
        LocalDateTime lastNotifiedAt,
        Long userId,
        Long currencyId
) {
}
