package com.company.currency.testdata;

import com.company.currency.domain.CurrencyRate;
import com.company.currency.dto.ExchangeRatesResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestData {

    public static ExchangeRatesResponse getExchangeRatesApiResponse(CurrencyRate currencyRate) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        Map<String, Map<String, BigDecimal>> rates = Map.of(yesterday.toString(), Map.of(currencyRate.getTarget(), currencyRate.getCurrentRate()));

        return ExchangeRatesResponse.builder()
            .startAt(yesterday)
            .endAt(yesterday)
            .base(currencyRate.getBase())
            .rates(rates)
            .build();
    }

    public static Map<String, Map<String, BigDecimal>> getExchangeRatesApiResponse(int amount, String target, LocalDate startAt) {
        return IntStream.range(0, amount)
            .mapToObj(i -> startAt.plusDays(i).toString())
            .collect(Collectors.toMap(Function.identity(), (day) -> Map.of(target, BigDecimal.valueOf(day.charAt(day.length() - 1)))));
    }
}
