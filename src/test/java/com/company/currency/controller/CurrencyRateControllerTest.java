package com.company.currency.controller;

import com.company.currency.client.ExchangeRatesApi;
import com.company.currency.domain.CurrencyRate;
import com.company.currency.domain.Query;
import com.company.currency.domain.Trend;
import com.company.currency.dto.CurrencyRateDTO;
import com.company.currency.dto.ErrorResponse;
import com.company.currency.dto.ExchangeRatesResponse;
import com.company.currency.repository.QueryRepository;
import com.company.currency.testdata.TestData;
import com.company.currency.util.DateUtils;
import io.restassured.http.ContentType;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "/sql/create-test-data.sql")
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/sql/delete-test-data.sql")
@AutoConfigureEmbeddedDatabase(provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.DOCKER)
public class CurrencyRateControllerTest {

    @Value("http://localhost:${local.server.port}/api/exchange-rate")
    private String baseUrl;

    @Autowired
    private QueryRepository queryRepository;

    @MockBean
    private ExchangeRatesApi exchangeRatesApi;

    private CurrencyRate currencyRate;
    private ExchangeRatesResponse exchangeRatesResponse;

    @BeforeEach
    void setUp() {
        queryRepository.deleteAll();

        BigDecimal value = BigDecimal.valueOf(RandomUtils.nextFloat(1, 20));
        value = value.setScale(2, RoundingMode.HALF_UP);
        currencyRate = CurrencyRate.builder()
            .currentRate(value)
            .average(value)
            .base("EUR")
            .target("USD")
            .build();
        exchangeRatesResponse = TestData.getExchangeRatesApiResponse(currencyRate);

        LocalDate date = LocalDate.now().minusDays(1);
        Mockito.when(exchangeRatesApi.getCurrencyRate(DateUtils.getStartDay(date, 5), date,
                                                      currencyRate.getBase(), currencyRate.getTarget()))
            .thenReturn(exchangeRatesResponse);
    }

    @Test
    void getTrend() {
        //@formatter:off
        CurrencyRateDTO response =
            given()
                .contentType(ContentType.JSON)
                .baseUri(baseUrl)
            .when()
                .get("/{date}/{base}/{target}", LocalDate.now().minusDays(1).toString(), "EUR", "USD")
                .prettyPeek()
            .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(CurrencyRateDTO.class);
        //@formatter:off
        assertEquals(currencyRate.getAverage(), response.getAverage());
        assertEquals(Trend.UNDEFINED.name(), response.getTrend());
        assertEquals(currencyRate.getCurrentRate(), response.getCurrentRate());
    }

    @Test
    void getHistoryOfMonth() {
        LocalDate now = LocalDate.now().minusDays(5);
        Query query = Query.builder()
            .currencyRates(Collections.singletonList(currencyRate))
            .base(currencyRate.getBase())
            .target(currencyRate.getTarget())
            .date(now)
            .build();
        queryRepository.save(query);

        //@formatter:off
        List<Query> response =
            given()
                .contentType(ContentType.JSON)
                .baseUri(baseUrl)
            .when()
                .get("/history/daily/{year}/{month}", now.getYear(), now.getMonthValue())
                .prettyPeek()
            .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .jsonPath()
                .getList("", Query.class);
        //@formatter:off
        assertEquals(1, response.size());
        assertEquals(currencyRate.getCurrentRate(), response.get(0).getCurrencyRates().get(0).getCurrentRate());
        assertEquals(currencyRate.getAverage(), response.get(0).getCurrencyRates().get(0).getAverage());
    }

    @Test
    void getHistoryOfDay() {
        LocalDate day = LocalDate.now().minusDays(2);
        Query query = Query.builder()
            .currencyRates(Collections.singletonList(currencyRate))
            .base(currencyRate.getBase())
            .target(currencyRate.getTarget())
            .date(day)
            .build();
        queryRepository.save(query);

        System.out.println("ALL QUERIES:");
        System.out.println(queryRepository.findAll());

        //@formatter:off
        List<Query> response =
            given()
                .contentType(ContentType.JSON)
                .baseUri(baseUrl)
            .when()
                .get("/history/daily/{year}/{month}/{day}",
                     day.getYear(),
                     day.getMonthValue(),
                     day.getDayOfMonth())
                .prettyPeek()
            .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .jsonPath()
                .getList("", Query.class);
        //@formatter:off
        assertEquals(1, response.size());
        assertEquals(response.get(0).getCurrencyRates().get(0).getCurrentRate(), currencyRate.getCurrentRate());
        assertEquals(response.get(0).getCurrencyRates().get(0).getAverage(), currencyRate.getAverage());
    }

    @Test
    void getTrendWrongDate() {
        //@formatter:off
        ErrorResponse response =
            given()
                .accept(ContentType.JSON)
                .baseUri(baseUrl)
            .when()
                .get("/{date}/{base}/{target}", LocalDate.now().toString(), "EUR", "USD")
                .prettyPeek()
            .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .body()
                .as(ErrorResponse.class);
        //@formatter:off

        assertNotNull(response.getTitle());
        assertNotNull(response.getDescription());
        assertEquals(400, response.getStatus());
    }

}
