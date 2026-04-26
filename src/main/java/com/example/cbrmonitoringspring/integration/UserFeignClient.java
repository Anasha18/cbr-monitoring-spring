package com.example.cbrmonitoringspring.integration;

import com.example.cbrmonitoringspring.integration.config.BaseConfiguration;
import com.example.cbrmonitoringspring.integration.dto.user.UserRequestDto;
import com.example.cbrmonitoringspring.integration.dto.user.UserResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "users",
        configuration = BaseConfiguration.class
)
public interface UserFeignClient {


    @RateLimiter(name = "users")
    @CircuitBreaker(name = "users")
    @Retry(name = "users")
    @GetMapping("/{id}")
    UserResponseDto findById(@PathVariable Long id);


    @RateLimiter(name = "users")
    @CircuitBreaker(name = "users")
    @Retry(name = "users")
    @PostMapping("/get-or-create")
    UserResponseDto getOrCreate(
            @Valid @RequestBody UserRequestDto dto
    );
}
