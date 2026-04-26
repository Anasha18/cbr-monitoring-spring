package com.example.cbrmonitoringspring.service;

import com.example.cbrmonitoringspring.integration.CurrencyFeignClient;
import com.example.cbrmonitoringspring.integration.dto.currency.CurrencyResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyFeignClient currencyFeignClient;

    public CurrencyResponseDto getCurrencyFromApiOrDb(String code) {
        return currencyFeignClient.getOrCreateCurrency(code);
    }

    public CurrencyResponseDto getCurrencyById(Long id) {
        return currencyFeignClient.getCurrencyById(id);
    }
}
