package com.example.cbrmonitoringspring.integration;

import com.example.cbrmonitoringspring.integration.config.BaseConfiguration;
import com.example.cbrmonitoringspring.integration.dto.subscription.SubscriptionRequestDto;
import com.example.cbrmonitoringspring.integration.dto.subscription.SubscriptionResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "subscriptions",
        configuration = BaseConfiguration.class
)
public interface SubscriptionFeignClient {


    @RateLimiter(name = "subscriptions")
    @CircuitBreaker(name = "subscriptions")
    @Retry(name = "subscriptions")
    @PostMapping
    void save(@RequestBody SubscriptionRequestDto dto);


    @RateLimiter(name = "subscriptions")
    @CircuitBreaker(name = "subscriptions")
    @Retry(name = "subscriptions")
    @GetMapping
    List<SubscriptionResponseDto> findAllSubscriptionsToNotify();


    @RateLimiter(name = "subscriptions")
    @CircuitBreaker(name = "subscriptions")
    @Retry(name = "subscriptions")
    @PutMapping("/{id}/last-notified-at")
    void updateLastNotifiedAt(@PathVariable Long id);


    @RateLimiter(name = "subscriptions")
    @CircuitBreaker(name = "subscriptions")
    @Retry(name = "subscriptions")
    @PostMapping("/subscribe")
    String subscribe(
            @RequestParam Long telegramId,
            @RequestParam String currencyCode
    );


    @RateLimiter(name = "subscriptions")
    @CircuitBreaker(name = "subscriptions")
    @Retry(name = "subscriptions")
    @PostMapping("/unsubscribe")
    String unsubscribe(
            @RequestParam Long telegramId,
            @RequestParam String currencyCode
    );
}
