package com.company.currency.util;

import com.company.currency.domain.CurrencyRate;
import com.company.currency.dto.CurrencyRateDTO;

import javax.validation.constraints.NotNull;

public final class BeanUtils {

    @NotNull
    public static CurrencyRateDTO toDTO(@NotNull CurrencyRate currencyRate) {
        return CurrencyRateDTO.builder()
            .average(currencyRate.getAverage())
            .currentRate(currencyRate.getCurrentRate())
            .trend(currencyRate.getTrend().toString())
            .build();
    }
}
