package com.example.backendbot.repository;

import com.example.backendbot.domain.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    Optional<ExchangeRate> findFirstByCurrencyIdOrderByRateDateDesc(Long currencyId);
    Optional<ExchangeRate> findFirstByCurrencyId(Long currencyId);
}
