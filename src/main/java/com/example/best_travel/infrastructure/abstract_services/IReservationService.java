package com.example.best_travel.infrastructure.abstract_services;

import com.example.best_travel.api.models.request.ReservationRequest;
import com.example.best_travel.api.models.responses.ReservationResponse;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

public interface IReservationService extends CrudService<ReservationRequest, ReservationResponse, UUID>{
     BigDecimal findPrice(Long hotelId, String currency);
}
