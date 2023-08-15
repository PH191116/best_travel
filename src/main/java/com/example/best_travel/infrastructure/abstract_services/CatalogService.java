package com.example.best_travel.infrastructure.abstract_services;

import com.example.best_travel.util.SortType;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface CatalogService <R>{
    Page<R> readAll(Integer page, Integer size, SortType sortType);
    List<R> readLessPrice(BigDecimal price);
    List<R> readBetweenPrice(BigDecimal min, BigDecimal max);
    String FIELD_BY_SORT = "price";
}
