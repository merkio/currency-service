package com.company.currency.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Currency rate service response")
public class CurrencyRateDTO {

    private BigDecimal currentRate;

    @ApiModelProperty("Average currency rate for the last five working days")
    private BigDecimal average;

    private String trend;
}
