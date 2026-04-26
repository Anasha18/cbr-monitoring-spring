package com.example.backendbot.controller;

import com.example.backendbot.controller.dto.subscription.SubscriptionRequestDto;
import com.example.backendbot.controller.dto.subscription.SubscriptionResponseDto;
import com.example.backendbot.domain.Subscription;
import com.example.backendbot.mapper.SubscriptionMapper;
import com.example.backendbot.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final SubscriptionMapper subscriptionMapper;

    @PostMapping
    public void save(@RequestBody SubscriptionRequestDto dto) {
        Subscription subscription = subscriptionMapper.toDomain(dto);
        subscriptionService.save(subscription);
    }

    @GetMapping
    public List<SubscriptionResponseDto> findAllSubscriptionsToNotify() {
        return subscriptionMapper.toDto(
                subscriptionService.findAllSubscriptionsToNotify()
        );
    }

    @PutMapping("/{id}/last-notified-at")
    public void updateLastNotifiedAt(@PathVariable Long id) {
        subscriptionService.updateLastNotifiedAt(id);
    }

    @PostMapping("/subscribe")
    public String subscribe(
            @RequestParam Long telegramId,
            @RequestParam String currencyCode
    ) {
        return subscriptionService.subscribe(telegramId, currencyCode);
    }

    @PostMapping("/unsubscribe")
    public String unsubscribe(
            @RequestParam Long telegramId,
            @RequestParam String currencyCode
    ) {
        return subscriptionService.unsubscribe(telegramId, currencyCode);
    }
}
