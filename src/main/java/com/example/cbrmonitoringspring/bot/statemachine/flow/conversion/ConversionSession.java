package com.example.cbrmonitoringspring.bot.statemachine.flow.conversion;


import com.example.cbrmonitoringspring.domain.Currency;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConversionSession {
    private ConversionStep conversionStep = ConversionStep.WAITING_CURRENCY_CODE_FROM;
    private Currency currencyFrom;
    private Currency currencyTo;
    private BigDecimal amount;
}
