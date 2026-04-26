package com.example.cbrmonitoringspring.service;

import com.example.cbrmonitoringspring.integration.SubscriptionFeignClient;
import com.example.cbrmonitoringspring.integration.dto.subscription.SubscriptionRequestDto;
import com.example.cbrmonitoringspring.integration.dto.subscription.SubscriptionResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionFeignClient subscriptionFeignClient;

    public void save(SubscriptionRequestDto dto) {
        subscriptionFeignClient.save(dto);
    }

    public String subscribe(Long telegramId, String currencyCode) {
        return subscriptionFeignClient.subscribe(telegramId, currencyCode);
    }

    public List<SubscriptionResponseDto> getAllSubscriptionsToNotify() {
        return subscriptionFeignClient.findAllSubscriptionsToNotify();
    }

    public void updateLastNotifiedAt(Long subscriptionId) {
        subscriptionFeignClient.updateLastNotifiedAt(subscriptionId);
    }

    public String unsubscribe(Long telegramId, String currencyCode) {
        return subscriptionFeignClient.unsubscribe(telegramId, currencyCode);
    }
}
