package com.example.backendbot.repository;

import com.example.backendbot.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {


    @Query("""
            from Subscription s
            join fetch s.currency c
            join fetch s.user u
            where u.id = :userId and c.id = :currencyId
            """)
    Optional<Subscription> findByUserIdAndCurrencyId(Long userId, Long currencyId);

    @Query("""
                            from Subscription s
                            join fetch s.currency
                            join fetch s.user
                            where s.isActive = true
                            and (s.lastNotifiedAt is null or s.lastNotifiedAt < :time)
            """)
    List<Subscription> findAllActiveNotifications(LocalDateTime time);
}
