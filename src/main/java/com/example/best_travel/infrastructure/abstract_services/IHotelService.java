package com.example.best_travel.infrastructure.abstract_services;

import com.example.best_travel.api.models.responses.HotelResponse;

import java.util.List;

public interface IHotelService extends CatalogService<HotelResponse>{
    List<HotelResponse> readByRating(Integer rating);
}
