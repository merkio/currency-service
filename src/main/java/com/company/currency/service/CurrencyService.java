package com.company.currency.service;

import com.company.currency.domain.Currency;
import com.company.currency.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService implements ICurrencyService {

    private final CurrencyRepository currencyRepository;

    @Override
    public boolean isExist(String code) {
        log.info("Check if currency [{}] is exist", code);
        return currencyRepository.findByCode(code).isPresent();
    }

    @Override
    public List<Currency> getAll() {
        log.info("Get all currencies");
        return currencyRepository.findAll();
    }

    @Override
    public void save(List<Currency> currencies) {
        log.info("Save list of currencies [{}]", currencies);
        currencyRepository.saveAll(currencies);
    }

    @Override
    public void removeByCode(String code) {
        log.info("Remove currency with code [{}]", code);
        currencyRepository.deleteByCode(code);
    }
}
