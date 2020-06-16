package com.company.currency.repository;

import com.company.currency.domain.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, String>, JpaSpecificationExecutor<CurrencyRate> {

}
