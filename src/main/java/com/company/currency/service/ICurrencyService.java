package com.company.currency.service;

import com.company.currency.domain.Currency;

import java.util.List;

public interface ICurrencyService {

    boolean isExist(String code);

    List<Currency> getAll();

    void save(List<Currency> currencies);

    void removeByCode(String code);
}
