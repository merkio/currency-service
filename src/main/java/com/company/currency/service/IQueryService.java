package com.company.currency.service;

import com.company.currency.domain.Query;

import java.util.List;

public interface IQueryService {

    void save(Query query);

    List<Query> getHistoryForTheDay(int year, int month, int day);

    List<Query> getHistoryForTheMonth(int year, int month);
}
