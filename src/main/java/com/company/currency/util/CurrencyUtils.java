package com.company.currency.util;

import com.company.currency.domain.Trend;
import com.google.common.collect.Ordering;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class CurrencyUtils {

    public static BigDecimal getAverageRate(Map<String, Map<String, BigDecimal>> rates) {
        BigDecimal sum = BigDecimal.ZERO;
        long count = 0;
        for (Map<String, BigDecimal> rate : rates.values()) {
            for (BigDecimal value : rate.values()) {
                sum = sum.add(value);
                count++;
            }
        }
        return sum.divide(BigDecimal.valueOf(count), RoundingMode.HALF_UP);
    }

    public static BigDecimal getCurrentRate(Map<String, Map<String, BigDecimal>> rates, LocalDate date, String target) {
        return rates.getOrDefault(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), Collections.emptyMap())
            .getOrDefault(target, BigDecimal.ZERO);
    }

    public static Trend getTrend(Map<String, Map<String, BigDecimal>> rates) {
        List<BigDecimal> dayRate = new ArrayList<>();
        rates = new TreeMap<>(rates);
        for (Map<String, BigDecimal> rate : rates.values()) {
            dayRate.addAll(rate.values());
        }
        boolean asc = dayRate.size() > 1 && Ordering.natural().isOrdered(dayRate);
        boolean desc = dayRate.size() > 1 && Ordering.natural().reverse().isOrdered(dayRate);
        boolean cons = dayRate.size() > 1 && new HashSet<>(dayRate).size() == 1;

        return  cons ? Trend.CONSTANT : asc ? Trend.ASCENDING : desc ? Trend.DESCENDING : Trend.UNDEFINED;
    }
}
