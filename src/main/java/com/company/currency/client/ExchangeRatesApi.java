package com.company.currency.client;

import com.company.currency.dto.ExchangeRatesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(value = "exchange-rates", url = "https://api.exchangeratesapi.io")
public interface ExchangeRatesApi {

    @GetMapping(
        value = "/history",
        produces = MediaType.APPLICATION_JSON_VALUE)
    ExchangeRatesResponse getCurrencyRate(@RequestParam("start_at") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startAt,
                                          @RequestParam("end_at") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endAt,
                                          @RequestParam("base") String base,
                                          @RequestParam(value = "symbols", required = false) String target);
}
