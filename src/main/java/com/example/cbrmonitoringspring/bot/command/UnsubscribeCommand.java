package com.example.cbrmonitoringspring.bot.command;

import com.example.cbrmonitoringspring.service.SubscriptionService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UnsubscribeCommand implements Command {
    private final SubscriptionService subscriptionService;

    @Override
    public String execute(Long telegramId, String... args) {
        if (args.length == 0) {
            return "Укажите код валюты: /unsubscribe <код валюты>";
        }
        String currencyCode = args[0];

        return subscriptionService.unsubscribe(telegramId, currencyCode);
    }
}
