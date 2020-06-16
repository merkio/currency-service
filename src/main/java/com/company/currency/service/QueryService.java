package com.company.currency.service;

import com.company.currency.domain.Query;
import com.company.currency.repository.QueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueryService implements IQueryService {

    private final QueryRepository queryRepository;

    @Override
    public void save(Query query) {
        log.info("Save query [{}]", query);
        queryRepository.save(query);
    }

    @Override
    public List<Query> getHistoryForTheDay(int year, int month, int day) {
        log.info("Get all query for the day [{}-{}-{}]", year, month, day);
        LocalDate from = LocalDate.of(year, month, day);
        return queryRepository.findAllByCreatedOnBetween(from.atStartOfDay(), from.plusDays(1).atStartOfDay());
    }

    @Override
    public List<Query> getHistoryForTheMonth(int year, int month) {
        log.info("Get all query for the month [{}-{}]", year, month);
        LocalDate from = LocalDate.of(year, month, 1);
        int lastDayOfTheMonth = new Calendar.Builder()
            .setDate(year, month, 1)
            .build()
            .getActualMaximum(Calendar.DAY_OF_MONTH);
        return queryRepository.findAllByCreatedOnBetween(from.atStartOfDay(),
                                                         from.plusDays(lastDayOfTheMonth).atStartOfDay());
    }
}
