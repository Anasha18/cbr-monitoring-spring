package com.example.cbrmonitoringspring.sheduler;

import com.example.cbrmonitoringspring.bot.CbrMonitoringBot;
import com.example.cbrmonitoringspring.domain.Subscription;
import com.example.cbrmonitoringspring.service.ExchangeRateService;
import com.example.cbrmonitoringspring.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduler {
    private final SubscriptionService subscriptionService;
    private final ExchangeRateService exchangeRateService;
    private final CbrMonitoringBot bot;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public void startNotification() {
        scheduler.scheduleAtFixedRate(this::sendNotification, 0, 1, TimeUnit.MINUTES);
    }

    private void sendNotification() {
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptionsToNotify();

        if (subscriptions.isEmpty()) {
            log.info("Нет подписок для уведомления");
            return;
        }

        for (Subscription subscription : subscriptions) {
            try {
                BigDecimal rate = exchangeRateService.getLatestCurrencyByCurrencyId(subscription.getCurrency().getId());

                String message = String.format("Курс %s (%s): %.2f руб.",
                        subscription.getCurrency().getCode(),
                        subscription.getCurrency().getName(),
                        rate);

                bot.sendMessage(subscription.getUser().getTelegramId(), message);

                subscriptionService.updateLastNotifiedAt(subscription);
                log.info("Уведомление отправлено пользователю {} по валюте {}",
                        subscription.getUser().getTelegramId(), subscription.getCurrency().getCode());

            } catch (Exception e) {
                log.error("Ошибка при отправке уведомления для подписки id={}", subscription.getId(), e);
            }
        }
    }
}
