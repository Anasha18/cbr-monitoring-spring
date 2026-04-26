package com.example.cbrmonitoringspring.bot.statemachine.flow.conversion;


import com.example.cbrmonitoringspring.integration.dto.currency.CurrencyResponseDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConversionSession {
    private ConversionStep conversionStep = ConversionStep.WAITING_CURRENCY_CODE_FROM;
    private CurrencyResponseDto currencyFrom;
    private CurrencyResponseDto currencyTo;
    private BigDecimal amount;
}
