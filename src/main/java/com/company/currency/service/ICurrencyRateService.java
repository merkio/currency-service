package com.company.currency.service;

import com.company.currency.dto.CurrencyRateDTO;

import java.time.LocalDate;

public interface ICurrencyRateService {

    CurrencyRateDTO getTrend(LocalDate date, String base, String target);
}
