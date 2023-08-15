package com.example.best_travel.infrastructure.abstract_services;

import com.example.best_travel.api.models.responses.FlyResponse;

import java.util.List;

public interface IFlyService extends CatalogService<FlyResponse>{
    List<FlyResponse> readByOriginDestiny(String origen, String destino);
}
