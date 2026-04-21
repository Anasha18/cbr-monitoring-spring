package com.example.cbrmonitoringspring.service;

import com.example.cbrmonitoringspring.domain.Currency;
import com.example.cbrmonitoringspring.domain.ExchangeRate;
import com.example.cbrmonitoringspring.repository.ExchangeRateRepository;
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

    @Cacheable(cacheNames = "exchangeRatesByCurrencyId", key = "#currencyId")
    public BigDecimal getLatestCurrencyByCurrencyId(Long currencyId) {
        return exchangeRateRepository.findLatestCurrencyByCurrencyId(currencyId)
                .map(ExchangeRate::getValue)
                .orElseThrow(() -> {
                    log.atError().addKeyValue("currencyId", currencyId).log("getLatestByCurrencyId failed");
                    return new RuntimeException("getLatestByCurrencyId failed");
                });
    }

    public Optional<ExchangeRate> getExchangeRateByCurrencyId(Long currencyId) {
        return exchangeRateRepository.findByCurrencyId(currencyId);
    }

    @Transactional
    @CacheEvict(cacheNames = "exchangeRatesByCurrencyId", key = "#currency.id")
    public void save(Currency currency) {
        Optional<ExchangeRate> exchangeRate = getExchangeRateByCurrencyId(currency.getId());
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
