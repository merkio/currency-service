package com.company.currency.service;

import com.company.currency.client.ExchangeRatesApi;
import com.company.currency.domain.CurrencyRate;
import com.company.currency.domain.Query;
import com.company.currency.domain.Trend;
import com.company.currency.repository.QueryRepository;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@Slf4j
@SpringBootTest
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "/sql/create-test-data.sql")
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/sql/delete-test-data.sql")
@AutoConfigureEmbeddedDatabase(provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.DOCKER)
public class QueryServiceTest {

    @Autowired
    private QueryRepository queryRepository;

    @Autowired
    private QueryService queryService;

    @MockBean
    private ExchangeRatesApi exchangeRatesApi;

    private CurrencyRate currencyRate;
    private Query query;

    @BeforeEach
    void setUp() {
        queryRepository.deleteAll();
        BigDecimal value = BigDecimal.valueOf(RandomUtils.nextDouble(0, 1));
        currencyRate = CurrencyRate.builder()
            .currentRate(value)
            .average(value)
            .base("EUR")
            .target("USD")
            .trend(Trend.UNDEFINED)
            .build();

        query = Query.builder()
            .currencyRates(Collections.singletonList(currencyRate))
            .build();

        queryService.save(query);
        query.setCreatedOn(LocalDateTime.now().minusDays(1));
        queryRepository.save(query);
        log.info("All queries in DB \n {}", queryRepository.findAll());
    }

    @Test
    void getHistoryForTheDay() {
        List<Query> queries = queryService.getHistoryForTheDay(query.getCreatedOn().getYear(),
                                                               query.getCreatedOn().getMonthValue(),
                                                               query.getCreatedOn().getDayOfMonth());

        assertTrue(queries.size() > 0);
    }

    @Test
    void getHistoryForTheMonth() {
        List<Query> queries = queryService.getHistoryForTheMonth(query.getCreatedOn().getYear(),
                                                                 query.getCreatedOn().getMonthValue());

        assertTrue(queries.size() > 0);
    }
}
