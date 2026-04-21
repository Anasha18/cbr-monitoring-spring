package com.example.cbrmonitoringspring.bot.command;

import com.example.cbrmonitoringspring.service.CurrencyService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CurrencyCommand implements Command {
    private final CurrencyService currencyService;

    @Override
    public String execute(Long telegramId, String... args) {
        String query = String.join(" ", args).trim();

        if (query.isEmpty()) {
            return """
                    Укажи название валюты:
                    /currency <код валюты>
                    """;
        }

        var found = currencyService.getCurrencyFromApiOrDb(query);

        return String.format("Курс %s (%s): %.2f руб.",
                found.getCode(),
                found.getName(),
                found.getValue());
    }
}
