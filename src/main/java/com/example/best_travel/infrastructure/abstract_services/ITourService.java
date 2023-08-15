package com.example.best_travel.infrastructure.abstract_services;

import com.example.best_travel.api.models.request.TourRequest;
import com.example.best_travel.api.models.responses.TourResponse;

import java.util.UUID;

public interface ITourService extends SimpleCrudService<TourRequest, TourResponse, Long> {
    void deleteTicket(Long tourId, UUID ticketId);
    UUID createTicket(Long flightId, Long tourId);
    void removeReservation(Long tourId, UUID reservationId);
    UUID createReservation(Long hotelId, Long tourId, Integer totalDays);
}
