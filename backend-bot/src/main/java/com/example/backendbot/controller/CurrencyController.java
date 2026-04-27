package com.example.backendbot.controller;

import com.example.backendbot.controller.dto.currency.CurrencyResponseDto;
import com.example.backendbot.domain.Currency;
import com.example.backendbot.mapper.CurrencyMapper;
import com.example.backendbot.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/currencies")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;
    private final CurrencyMapper currencyMapper;

    @PostMapping("get-or-create/{code}")
    public CurrencyResponseDto getOrCreateCurrency(@PathVariable String code) {
        Currency currency = currencyService.getCurrencyFromApiOrDb(code);
        return currencyMapper.toDto(currency);
    }

    @GetMapping("/{id}")
    public CurrencyResponseDto getCurrencyById(@PathVariable Long id) {
        Currency currency = currencyService.getCurrencyById(id);
        return currencyMapper.toDto(currency);
    }
}
