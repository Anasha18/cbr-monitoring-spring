package com.example.cbrmonitoringspring.service;

import com.example.cbrmonitoringspring.integration.ExchangeRateFeignClient;
import com.example.cbrmonitoringspring.integration.dto.currency.CurrencyRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final ExchangeRateFeignClient exchangeRateFeignClient;

    public BigDecimal findLatestCurrencyByCurrencyId(Long currencyId) {
        return exchangeRateFeignClient.findLatestCurrencyByCurrencyId(currencyId);
    }

    public void save(CurrencyRequestDto dto) {
        exchangeRateFeignClient.save(dto);
    }
}
