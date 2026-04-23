package com.example.cbrmonitoringspring.service;

import com.example.cbrmonitoringspring.domain.Currency;
import com.example.cbrmonitoringspring.domain.Subscription;
import com.example.cbrmonitoringspring.domain.User;
import com.example.cbrmonitoringspring.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final int NOTIFICATION_INTERVAL = 1;

    private final SubscriptionRepository subscriptionRepo;
    private final UserService userService;
    private final CurrencyService currencyService;

    @Transactional
    public void save(Subscription subscription) {
        subscriptionRepo.save(subscription);
    }

    @Transactional
    public String subscribe(Long telegramId, String currencyCode) {
        User foundedUser = userService.findByTelegramId(telegramId)
                .orElseThrow(() -> new RuntimeException("User not found with telegramId: " + telegramId));

        Currency foundedCurrency = currencyService.getCurrencyFromApiOrDb(currencyCode);

        Optional<Subscription> foundedSubscription = subscriptionRepo.findByUserIdAndCurrencyId(foundedUser.getId(), foundedCurrency.getId());

        if (foundedSubscription.isPresent()) {
            var subscription = foundedSubscription.get();

            if (subscription.getIsActive()) {
                return "Вы уже подписаны на: " + currencyCode;
            } else {
                subscription.setIsActive(true);
                subscription.setLastNotifiedAt(null);
                save(subscription);

                return "Подписка на: " + currencyCode + " активна.";
            }
        } else {
            Subscription subscription = Subscription.builder()
                    .isActive(true)
                    .lastNotifiedAt(null)
                    .user(foundedUser)
                    .currency(foundedCurrency)
                    .build();

            save(subscription);
            return "Вы подписались на " + currencyCode + ". Уведомления будут приходить каждую минуту.";
        }
    }

    public List<Subscription> getAllSubscriptionsToNotify() {
        LocalDateTime time = LocalDateTime.now().minusMinutes(NOTIFICATION_INTERVAL);
        return subscriptionRepo.findAllActiveNotifications(time);
    }

    @Transactional
    public void updateLastNotifiedAt(Subscription subscription) {
        subscription.setLastNotifiedAt(LocalDateTime.now());
        save(subscription);
    }

    @Transactional
    public String unsubscribe(Long telegramId, String currencyCode) {
        User foundedUser = userService.findByTelegramId(telegramId)
                .orElseThrow(() -> new RuntimeException("User not found with telegramId: " + telegramId));

        Currency foundedCurrency = currencyService.getCurrencyFromApiOrDb(currencyCode);

        Optional<Subscription> foundedSubscription = subscriptionRepo
                .findByUserIdAndCurrencyId(foundedUser.getId(), foundedCurrency.getId());

        if (foundedSubscription.isEmpty()) {
            return "У вас нет подписки на валюту " + currencyCode;
        }

        Subscription subscription = foundedSubscription.get();
        if (!subscription.getIsActive()) {
            return "Подписка на " + currencyCode + " уже неактивна";
        }

        subscription.setIsActive(false);
        updateLastNotifiedAt(subscription);

        return "Подписка на " + currencyCode + " отключена.";

    }
}
