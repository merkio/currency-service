package com.company.currency.service;

import com.company.currency.client.ExchangeRatesApi;
import com.company.currency.domain.CurrencyRate;
import com.company.currency.domain.Query;
import com.company.currency.domain.Trend;
import com.company.currency.dto.CurrencyRateDTO;
import com.company.currency.dto.ExchangeRatesResponse;
import com.company.currency.exception.IllegalArgumentsException;
import com.company.currency.repository.QueryRepository;
import com.company.currency.testdata.TestData;
import com.company.currency.util.DateUtils;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@Transactional
@SpringBootTest
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "/sql/create-test-data.sql")
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/sql/delete-test-data.sql")
@AutoConfigureEmbeddedDatabase(provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.DOCKER)
class CurrencyRateServiceTest {

    @Autowired
    private QueryRepository queryRepository;

    @Autowired
    private QueryService queryService;

    @Autowired
    private CurrencyRateService currencyRateService;

    @MockBean
    private ExchangeRatesApi exchangeRatesApi;

    private CurrencyRate currencyRate;
    private ExchangeRatesResponse exchangeRatesResponse;
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
            .build();
    }

    @Test
    void createCurrencyRate() {
        exchangeRatesResponse = TestData.getExchangeRatesApiResponse(currencyRate);

        LocalDate date = LocalDate.now().minusDays(1);
        Mockito.when(exchangeRatesApi.getCurrencyRate(DateUtils.getStartDay(date, 5), date,
                                                      currencyRate.getBase(), currencyRate.getTarget())).thenReturn(exchangeRatesResponse);

        CurrencyRateDTO currencyRateDTO = currencyRateService.getTrend(date, currencyRate.getBase(), currencyRate.getTarget());

        assertEquals(currencyRate.getAverage(), currencyRateDTO.getAverage());
        assertEquals(currencyRate.getCurrentRate(), currencyRateDTO.getCurrentRate());
        assertEquals(Trend.CONSTANT.name(), currencyRateDTO.getTrend());
    }

    @Test
    void createCurrencyRateWrongDate() {
        LocalDate date = LocalDate.of(1999, 12, 29);
        Mockito.when(exchangeRatesApi.getCurrencyRate(any(), any(), any(), any())).thenReturn(exchangeRatesResponse);

        assertThrows(IllegalArgumentsException.class, () ->
            currencyRateService.getTrend(date, currencyRate.getBase(), currencyRate.getTarget()));
    }

    @Test
    void createCurrencyRateWrongDay() {
        LocalDate date = LocalDate.now();
        Mockito.when(exchangeRatesApi.getCurrencyRate(any(), any(), any(), any())).thenReturn(exchangeRatesResponse);

        assertThrows(IllegalArgumentsException.class, () ->
            currencyRateService.getTrend(date, currencyRate.getBase(), currencyRate.getTarget()));
    }

    @Test
    void createCurrencyRateWrongBase() {
        LocalDate date = LocalDate.now().minusDays(3);
        Mockito.when(exchangeRatesApi.getCurrencyRate(any(), any(), any(), any())).thenReturn(exchangeRatesResponse);

        assertThrows(IllegalArgumentsException.class, () ->
            currencyRateService.getTrend(date, "XXX", currencyRate.getTarget()));
    }

    @Test
    void createCurrencyRateForPeriod() {
        String base = "EUR";
        String target = "USD";
        LocalDate yesterday = LocalDate.now().minusDays(1);
        ExchangeRatesResponse ratesResponse = ExchangeRatesResponse.builder()
            .rates(TestData.getExchangeRatesApiResponse(5, target, yesterday.minusDays(5)))
            .base(base)
            .startAt(yesterday.minusDays(5))
            .endAt(yesterday)
            .build();
        Mockito.when(exchangeRatesApi.getCurrencyRate(any(), any(), any(), any())).thenReturn(ratesResponse);

        CurrencyRateDTO result = currencyRateService.getTrend(yesterday, base, target);
        assertEquals(Trend.ASCENDING.name(), result.getTrend());
    }
}