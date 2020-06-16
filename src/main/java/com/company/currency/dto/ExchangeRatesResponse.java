package com.company.currency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRatesResponse {

    private Map<String, Map<String, BigDecimal>> rates;

    @JsonProperty("start_at")
    private LocalDate startAt;

    private String base;

    @JsonProperty("end_at")
    private LocalDate endAt;
}
