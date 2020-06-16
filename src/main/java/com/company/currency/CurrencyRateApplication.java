package com.company.currency;

import com.company.currency.client.ExchangeRatesApi;
import com.company.currency.domain.Currency;
import com.company.currency.dto.ExchangeRatesResponse;
import com.company.currency.repository.CurrencyRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@EnableJpaAuditing
@SpringBootApplication
@EnableFeignClients(basePackages = "com.company.currency.client")
public class CurrencyRateApplication {

    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(CurrencyRateApplication.class, args);

        CurrencyRepository currencyRepository = context.getBean(CurrencyRepository.class);
        ExchangeRatesApi exchangeRatesApi = context.getBean(ExchangeRatesApi.class);

        if (currencyRepository.findAll().isEmpty()) {

            // add currencies from source
            LocalDate yesterday = LocalDate.now().minusDays(2);
            ExchangeRatesResponse ratesResponse = exchangeRatesApi.getCurrencyRate(yesterday, yesterday.plusDays(1), "EUR", null);

            Set<String> codes = new HashSet<>();
            codes.add("EUR");
            for (Map<String, BigDecimal> map : ratesResponse.getRates().values()) {
                codes.addAll(map.keySet());
            }
            currencyRepository.saveAll(codes.stream()
                                           .map(code -> Currency.builder().code(code).build())
                                           .collect(Collectors.toList()));
        }
    }
}
