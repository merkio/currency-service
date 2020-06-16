package com.company.currency.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "currency_rates", name = "currency_rates")
public class CurrencyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "currency_rates_generator")
    @SequenceGenerator(name = "currency_rates_generator",
        sequenceName = "currency_rates.currency_rates_sequence", schema = "currency_rates")
    private Long id;

    @Column(precision = 10, scale = 2)
    private BigDecimal currentRate;

    @Column(precision = 10, scale = 2)
    private BigDecimal average;

    @Enumerated(EnumType.STRING)
    private Trend trend;

    private String base;

    private String target;
}
