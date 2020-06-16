package com.company.currency.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "currency_rates", name = "currencies")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "currencies_generator")
    @SequenceGenerator(name = "currencies_generator",
        sequenceName = "currency_rates.currencies_sequence", schema = "currency_rates")
    private Long id;

    private String code;
}
