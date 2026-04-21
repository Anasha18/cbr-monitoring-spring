package com.example.cbrmonitoringspring.repository;

import com.example.cbrmonitoringspring.domain.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {


    @Query("""
            from ExchangeRate r
            join fetch r.currency c
            where c.id = :currencyId
            order by r.rateDate desc
            """)
    Optional<ExchangeRate> findLatestCurrencyByCurrencyId(Long currencyId);


    @Query("""
            from ExchangeRate r
            join fetch r.currency c
            where c.id = :currencyId
            """)
    Optional<ExchangeRate> findByCurrencyId(Long currencyId);
}
