package com.example.cbrmonitoringspring.sheduler;

import com.example.cbrmonitoringspring.bot.CbrMonitoringBot;
import com.example.cbrmonitoringspring.integration.dto.currency.CurrencyResponseDto;
import com.example.cbrmonitoringspring.integration.dto.subscription.SubscriptionResponseDto;
import com.example.cbrmonitoringspring.integration.dto.user.UserResponseDto;
import com.example.cbrmonitoringspring.service.CurrencyService;
import com.example.cbrmonitoringspring.service.ExchangeRateService;
import com.example.cbrmonitoringspring.service.SubscriptionService;
import com.example.cbrmonitoringspring.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduler {
    private final SubscriptionService subscriptionService;
    private final ExchangeRateService exchangeRateService;
    private final CurrencyService currencyService;
    private final UserService userService;
    private final CbrMonitoringBot bot;

    @Scheduled(
            fixedDelayString = "${app.notifications.fixed-delay-ms:60000}",
            initialDelayString = "${app.notifications.initial-delay-ms:10000}"
    )
    public void sendNotification() {
        List<SubscriptionResponseDto> subscriptions = subscriptionService.getAllSubscriptionsToNotify();

        if (subscriptions.isEmpty()) {
            log.debug("No subscriptions to notify");
            return;
        }

        for (SubscriptionResponseDto subscription : subscriptions) {
            try {
                BigDecimal rate = exchangeRateService.findLatestCurrencyByCurrencyId(subscription.currencyId());
                CurrencyResponseDto currency = currencyService.getCurrencyById(subscription.currencyId());
                UserResponseDto user = userService.findById(subscription.userId());

                String message = String.format("Курс %s (%s): %.2f руб.",
                        currency.code(),
                        currency.name(),
                        rate);

                bot.sendMessage(user.telegramId(), message);

                subscriptionService.updateLastNotifiedAt(subscription.id());
                log.info("Notification sent to user {} for currency {}", user.telegramId(), currency.code());
            } catch (Exception e) {
                log.error("Failed to send notification for subscription id={}", subscription.id(), e);
            }
        }
    }
}
