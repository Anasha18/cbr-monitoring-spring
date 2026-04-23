package com.example.cbrmonitoringspring.service;

import com.example.cbrmonitoringspring.domain.Currency;
import com.example.cbrmonitoringspring.integration.CbrFeignClient;
import com.example.cbrmonitoringspring.integration.dto.CurrencyResponse;
import com.example.cbrmonitoringspring.mapper.CurrencyMapper;
import com.example.cbrmonitoringspring.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final ExchangeRateService exchangeRateService;
    private final CbrFeignClient feign;

    @Transactional
    @CachePut(cacheNames = "currenciesByCode", key = "#result.code.toUpperCase()")
    public Currency save(Currency currency) {
        return currencyRepository.save(currency);
    }

    @Cacheable(cacheNames = "currenciesByCode", key = "#code.toUpperCase()")
    public Currency getCurrencyFromApiOrDb(String code) {
        String normalizedCode = code.toUpperCase();
        Optional<Currency> foundedCurrency = currencyRepository.findByCode(normalizedCode);

        if (foundedCurrency.isPresent()) {
            log.info("Currencies found for currency code at db {}", normalizedCode);
            return foundedCurrency.get();
        }

        CurrencyResponse currency = feign.getCurrencies();

        CurrencyResponse.CBRCurrencyData foundCurrency = currency
                .valute()
                .values()
                .stream()
                .filter(c -> c.charCode().equalsIgnoreCase(normalizedCode))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("currency not found by code: " + normalizedCode));

        Currency currencyToDomain = CurrencyMapper.toDomain(foundCurrency);
        save(currencyToDomain);

        return currencyToDomain;
    }
}
