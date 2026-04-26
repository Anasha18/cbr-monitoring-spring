package com.example.cbrmonitoringspring.integration.dto.subscription;

import java.time.LocalDateTime;

public record SubscriptionRequestDto(
        Boolean isActive,
        LocalDateTime lastNotifiedAt,
        Long userId,
        Long currencyId
) {
}
