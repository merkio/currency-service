package com.company.currency.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

public final class DateUtils {

    public static LocalDate getStartDay(LocalDate date, int days) {
        DayOfWeek dayOfWeek = DayOfWeek.of(date.get(ChronoField.DAY_OF_WEEK));
        while (days > 0) {
            switch (dayOfWeek) {
                case MONDAY:
                    date = date.minus(3, ChronoUnit.DAYS);
                case SUNDAY:
                    date = date.minus(2, ChronoUnit.DAYS);
                default:
                    date = date.minus(1, ChronoUnit.DAYS);

            }
            days--;
        }
        return date;
    }

}
