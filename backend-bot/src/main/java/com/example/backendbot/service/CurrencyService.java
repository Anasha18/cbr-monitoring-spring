package com.example.backendbot.service;

import com.example.backendbot.domain.Currency;
import com.example.backendbot.exception.NotFoundException;
import com.example.backendbot.integration.CbrFeignClient;
import com.example.backendbot.integration.dto.CurrencyResponse;
import com.example.backendbot.mapper.CurrencyMapper;
import com.example.backendbot.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;
    private final CbrFeignClient feign;

    @Transactional
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

        Currency currencyToDomain = currencyMapper.toDomain(foundCurrency);
        currencyRepository.save(currencyToDomain);

        return currencyToDomain;
    }

    public Currency getCurrencyById(Long id) {
        return currencyRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("currency not found by id: " + id));
    }
}
