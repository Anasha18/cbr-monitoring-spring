package com.example.backendbot.service;

import com.example.backendbot.controller.dto.currency.CurrencyRequestDto;
import com.example.backendbot.domain.ExchangeRate;
import com.example.backendbot.mapper.CurrencyMapper;
import com.example.backendbot.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyMapper currencyMapper;

    @Cacheable(cacheNames = "exchangeRatesByCurrencyId", key = "#currencyId")
    public BigDecimal findLatestCurrencyByCurrencyId(Long currencyId) {
        return exchangeRateRepository.findLatestCurrencyByCurrencyId(currencyId)
                .map(ExchangeRate::getValue)
                .orElseThrow(() -> {
                    log.atError().addKeyValue("currencyId", currencyId).log("getLatestByCurrencyId failed");
                    return new RuntimeException("getLatestByCurrencyId failed");
                });
    }

    public Optional<ExchangeRate> findExchangeRateByCurrencyId(Long currencyId) {
        return exchangeRateRepository.findByCurrencyId(currencyId);
    }

    @Transactional
    @CacheEvict(cacheNames = "exchangeRatesByCurrencyId", key = "#dto.code()")
    public void save(CurrencyRequestDto dto) {
        var currency = currencyMapper.toDomain(dto);

        Optional<ExchangeRate> exchangeRate = findExchangeRateByCurrencyId(currency.getId());
        if (exchangeRate.isPresent()) {
            var rate = exchangeRate.get();
            rate.setValue(currency.getValue());
            exchangeRateRepository.save(rate);
        } else {
            ExchangeRate newRate = ExchangeRate.builder()
                    .currency(currency)
                    .value(currency.getValue())
                    .rateDate(LocalDateTime.now())
                    .build();
            exchangeRateRepository.save(newRate);
        }
    }
}
