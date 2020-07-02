package com.company.currency.controller;

import com.company.currency.domain.Query;
import com.company.currency.dto.CurrencyRateDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CurrencyRateApi {

    CurrencyRateDTO getTrend(LocalDate date, String base, String target);

    List<Query> getQueryHistoryForPeriod(int year, int month, Optional<Integer> day);
}
