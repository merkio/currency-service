package com.company.currency.controller;

import com.company.currency.domain.Query;
import com.company.currency.dto.CurrencyRateDTO;
import com.company.currency.service.CurrencyRateService;
import com.company.currency.service.QueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exchange-rate")
public class CurrencyRateController implements CurrencyRateApi {

    private final CurrencyRateService currencyRateService;
    private final QueryService queryService;

    @Override
    @GetMapping(
        value = "/{date}/{baseCurrency}/{targetCurrency}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public CurrencyRateDTO getTrend(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                    @PathVariable("baseCurrency") String base,
                                    @PathVariable("targetCurrency") String target) {
        return currencyRateService.getTrend(date, base, target);
    }

    @Override
    @GetMapping(
        value = {
            "/history/daily/{year}/{month}",
            "/history/daily/{year}/{month}/{day}"},
        produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Query> getQueryHistoryForTheDay(@PathVariable("year") int year,
                                                @PathVariable("month") int month,
                                                @PathVariable("day") Optional<Integer> dayO) {
        return dayO.map(day -> queryService.getHistoryForTheDay(year, month, day))
            .orElse(queryService.getHistoryForTheMonth(year, month));
    }
}
