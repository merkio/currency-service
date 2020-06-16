package com.company.currency.service;

import com.company.currency.client.ExchangeRatesApi;
import com.company.currency.domain.CurrencyRate;
import com.company.currency.domain.Query;
import com.company.currency.dto.CurrencyRateDTO;
import com.company.currency.dto.ExchangeRatesResponse;
import com.company.currency.exception.IllegalArgumentsException;
import com.company.currency.util.BeanUtils;
import com.company.currency.util.CurrencyUtils;
import com.company.currency.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyRateService implements ICurrencyRateService {

    private final ICurrencyService currencyService;
    private final IQueryService queryService;
    private final ExchangeRatesApi exchangeRatesApi;

    @Override
    public CurrencyRateDTO getTrend(LocalDate date, String base, String target) {
        log.info("Get trend for five days until [{}] with base [{}] and target [{}]", date, base, target);

        if (!currencyService.isExist(base) || !currencyService.isExist(target)) {
            IllegalArgumentsException.throwException("Incorrect currencies: {} or {}", base, target);
        }
        if (date.isBefore(LocalDate.of(2000, 1, 1)) || date.isAfter(LocalDate.now().minusDays(1))) {
            IllegalArgumentsException.throwException("Incorrect date: {}", date);
        }

        ExchangeRatesResponse ratesResponse = exchangeRatesApi.getCurrencyRate(DateUtils.getStartDay(date, 5), date, base, target);
        log.info("Get response from exchangeRateApi\n {}", ratesResponse);
        CurrencyRate currencyRate = CurrencyRate.builder()
            .base(base)
            .target(target)
            .average(CurrencyUtils.getAverageRate(ratesResponse.getRates()))
            .currentRate(CurrencyUtils.getCurrentRate(ratesResponse.getRates(), date, target))
            .trend(CurrencyUtils.getTrend(ratesResponse.getRates()))
            .build();
        Query query = Query.builder().currencyRates(Collections.singletonList(currencyRate)).build();

        queryService.save(query);

        return BeanUtils.toDTO(currencyRate);
    }
}
