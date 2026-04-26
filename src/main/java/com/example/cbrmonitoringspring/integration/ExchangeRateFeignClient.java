package com.example.cbrmonitoringspring.integration;

import com.example.cbrmonitoringspring.integration.config.BaseConfiguration;
import com.example.cbrmonitoringspring.integration.dto.currency.CurrencyRequestDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

@FeignClient(
        name = "rates",
        configuration = BaseConfiguration.class
)
public interface ExchangeRateFeignClient {

    @RateLimiter(name = "rates")
    @CircuitBreaker(name = "rates")
    @Retry(name = "rates")
    @GetMapping("/{currencyId}")
    BigDecimal findLatestCurrencyByCurrencyId(@PathVariable Long currencyId);


    @RateLimiter(name = "rates")
    @CircuitBreaker(name = "rates")
    @Retry(name = "rates")
    @PostMapping
    void save(@Valid @RequestBody CurrencyRequestDto dto);
}
