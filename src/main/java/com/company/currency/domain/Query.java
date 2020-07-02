package com.company.currency.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(schema = "currency_rates", name = "queries")
public class Query {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "queries_generator")
    @SequenceGenerator(name = "queries_generator",
        sequenceName = "currency_rates.queries_sequence", schema = "currency_rates")
    private Long id;

    @CreatedDate
    private LocalDateTime createdOn;

    private String base;

    private String target;

    private LocalDate date;

    @JoinColumn(name = "query_id")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CurrencyRate> currencyRates;
}
