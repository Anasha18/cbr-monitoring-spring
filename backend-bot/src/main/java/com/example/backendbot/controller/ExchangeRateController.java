package com.example.backendbot.controller;

import com.example.backendbot.controller.dto.currency.CurrencyRequestDto;
import com.example.backendbot.service.ExchangeRateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/rates")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @GetMapping("/{currencyId}")
    public BigDecimal findLatestCurrencyByCurrencyId(@PathVariable Long currencyId) {
        return exchangeRateService.findLatestCurrencyByCurrencyId(currencyId);
    }

    @PostMapping
    public void save(@Valid @RequestBody CurrencyRequestDto dto) {
        exchangeRateService.save(dto);
    }
}
