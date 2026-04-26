package com.example.cbrmonitoringspring.bot.statemachine.flow.conversion;

import com.example.cbrmonitoringspring.bot.command.ConversionCommand;
import com.example.cbrmonitoringspring.bot.statemachine.BotStateMachine;
import com.example.cbrmonitoringspring.bot.statemachine.StateMachineResult;
import com.example.cbrmonitoringspring.integration.dto.currency.CurrencyResponseDto;
import com.example.cbrmonitoringspring.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Component
public class ConversionFlow implements BotStateMachine {
    private final CurrencyService currencyService;

    private Map<Long, ConversionSession> sessions = new ConcurrentHashMap<>();

    @Override
    public StateMachineResult startFlow(Long chatId, String flowKey) {
        if (!ConversionCommand.CONVERSION_FLOW_KEY.equals(flowKey)) {
            return StateMachineResult.NOT_IMPLEMENTED;
        }

        if (sessions.containsKey(chatId)) {
            return StateMachineResult.ALREADY_RUNNING;
        }

        sessions.put(chatId, new ConversionSession());

        return StateMachineResult.STARTED;
    }

    @Override
    public boolean hasActiveFlow(Long chatId) {
        return sessions.containsKey(chatId);
    }

    @Override
    public Optional<String> tryHandle(Long chatId, String incomingText) {
        ConversionSession conversionSession = sessions.get(chatId);

        if (conversionSession == null) {
            return Optional.empty();
        }

        String text = incomingText.trim();

        return switch (conversionSession.getConversionStep()) {
            case WAITING_CURRENCY_CODE_FROM -> Optional.of(handleCurrencyFrom(conversionSession, text));
            case WAITING_CURRENCY_CODE_TO -> Optional.of(handleCurrencyTo(conversionSession, text));
            case WAITING_AMOUNT -> Optional.of(handleAmount(chatId, conversionSession, text));
        };
    }

    private String handleCurrencyFrom(ConversionSession conversionSession, String text) {
        CurrencyResponseDto currencyFrom = currencyService.getCurrencyFromApiOrDb(text);

        if (currencyFrom == null) {
            return "Такой валлюты нет...";
        }

        conversionSession.setCurrencyFrom(currencyFrom);
        conversionSession.setConversionStep(ConversionStep.WAITING_CURRENCY_CODE_TO);

        return "Введите в какую валюту перевести.";
    }

    private String handleCurrencyTo(ConversionSession conversionSession, String text) {
        CurrencyResponseDto currencyTo = currencyService.getCurrencyFromApiOrDb(text);

        if (currencyTo == null) {
            return "Такой валлюты нет...";
        }

        conversionSession.setCurrencyTo(currencyTo);
        conversionSession.setConversionStep(ConversionStep.WAITING_AMOUNT);

        return "Введите сумму перевода.";
    }

    private String handleAmount(Long chatId, ConversionSession conversionSession, String text) {
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(text));

        CurrencyResponseDto currencyFrom = conversionSession.getCurrencyFrom();
        CurrencyResponseDto currencyTo = conversionSession.getCurrencyTo();

        if (currencyFrom == null || currencyTo == null) {
            conversionSession.setConversionStep(ConversionStep.WAITING_CURRENCY_CODE_FROM);
            return "Сначала выберите исходную  валюты. Начните заново.";
        }

        BigDecimal result = processConvertCurrency(currencyFrom, currencyTo, amount);
        sessions.remove(chatId);

        return String.format("%.2f %s = %.2f %s",
                amount, currencyFrom.code(), result, currencyTo.code());
    }

    private BigDecimal processConvertCurrency(
            CurrencyResponseDto currencyFrom,
            CurrencyResponseDto currencyTo,
            BigDecimal amount
    ) {
        BigDecimal valuteCurrencyFrom = currencyFrom.value();
        BigDecimal valuteCurrencyTo = currencyTo.value();

        BigDecimal amountInBase = amount.multiply(valuteCurrencyFrom);

        return amountInBase.divide(valuteCurrencyTo, 4, RoundingMode.HALF_UP);
    }

}
