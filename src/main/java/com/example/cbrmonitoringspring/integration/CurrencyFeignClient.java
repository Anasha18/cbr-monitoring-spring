package com.example.cbrmonitoringspring.integration;

import com.example.cbrmonitoringspring.integration.config.BaseConfiguration;
import com.example.cbrmonitoringspring.integration.dto.currency.CurrencyResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "currencies",
        configuration = BaseConfiguration.class
)
public interface CurrencyFeignClient {

    @RateLimiter(name = "currencies")
    @CircuitBreaker(name = "currencies")
    @Retry(name = "currencies")
    @PostMapping("get-or-create/{code}")
    CurrencyResponseDto getOrCreateCurrency(@PathVariable String code);


    @RateLimiter(name = "currencies")
    @CircuitBreaker(name = "currencies")
    @Retry(name = "currencies")
    @GetMapping("/{id}")
    CurrencyResponseDto getCurrencyById(@PathVariable Long id);

}
