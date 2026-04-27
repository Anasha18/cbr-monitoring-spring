package com.example.backendbot.integration;


import com.example.backendbot.integration.config.CbrFeignConfig;
import com.example.backendbot.integration.dto.CurrencyResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "cbr-api",
        url = "${spring.cloud.openfeign.client.config.cbr-api.url}",
        configuration = CbrFeignConfig.class
)
public interface CbrFeignClient {

    @RateLimiter(name = "cbr-api")
    @CircuitBreaker(name = "cbr-api")
    @Retry(name = "cbr-api")
    @GetMapping(value = "/daily_json.js", consumes = {"application/javascript"})
    CurrencyResponse getCurrencies();
}
