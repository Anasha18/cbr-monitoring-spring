package com.example.backendbot.service;

import com.example.backendbot.controller.dto.currency.CurrencyRequestDto;
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
    private final ExchangeRateService exchangeRateService;
    private final CurrencyMapper currencyMapper;
    private final CbrFeignClient feign;

    @Transactional
    @Cacheable(cacheNames = "currenciesByCode", key = "#code.toUpperCase()")
    public Currency getCurrencyFromApiOrDb(String code) {
        String normalizedCode = code.toUpperCase();
        Optional<Currency> foundedCurrency = currencyRepository.findByCode(normalizedCode);

        if (foundedCurrency.isPresent()) {
            Currency currency = foundedCurrency.get();
            log.info("Currencies found for currency code at db {}", normalizedCode);
            exchangeRateService.save(new CurrencyRequestDto(
                    currency.getCode(),
                    currency.getName(),
                    currency.getValue()
            ));
            return currency;
        }

        CurrencyResponse currency = feign.getCurrencies();

        CurrencyResponse.CBRCurrencyData foundCurrency = foundCurrencyByCode(currency, normalizedCode);

        Currency currencyToDomain = currencyMapper.toDomain(foundCurrency);
        currencyRepository.save(currencyToDomain);
        exchangeRateService.save(new CurrencyRequestDto(
                currencyToDomain.getCode(),
                currencyToDomain.getName(),
                currencyToDomain.getValue()
        ));

        return currencyToDomain;
    }

    private CurrencyResponse.CBRCurrencyData foundCurrencyByCode(CurrencyResponse response, String code) {
        return response
                .valute()
                .values()
                .stream()
                .filter(c -> c.charCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("currency not found by code: " + code));
    }

    public Currency getCurrencyById(Long id) {
        return currencyRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("currency not found by id: " + id));
    }
}
