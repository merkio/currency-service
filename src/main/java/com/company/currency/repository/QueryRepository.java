package com.company.currency.repository;

import com.company.currency.domain.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QueryRepository extends JpaRepository<Query, Long>, JpaSpecificationExecutor<Query> {

    List<Query> findAllByCreatedOnBetween(LocalDateTime from, LocalDateTime to);
}
