package com.example.best_travel.infrastructure.abstract_services.service;

import com.example.best_travel.api.models.request.TourRequest;
import com.example.best_travel.api.models.responses.TourResponse;
import com.example.best_travel.domain.entities.jpa.*;
import com.example.best_travel.domain.repositories.CustomerRepository;
import com.example.best_travel.domain.repositories.FlyRepository;
import com.example.best_travel.domain.repositories.HotelRepository;
import com.example.best_travel.domain.repositories.TourRepository;
import com.example.best_travel.infrastructure.abstract_services.ITourService;
import com.example.best_travel.infrastructure.abstract_services.helpers.BlackListHelper;
import com.example.best_travel.infrastructure.abstract_services.helpers.CustomerHelper;
import com.example.best_travel.infrastructure.abstract_services.helpers.EmailHelper;
import com.example.best_travel.infrastructure.abstract_services.helpers.TourHelper;
import com.example.best_travel.util.Tables;
import com.example.best_travel.util.exceptions.IdNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
@AllArgsConstructor
public class TourService implements ITourService {
    private final HotelRepository hotelRepository;
    private final FlyRepository flyRepository;
    private final CustomerRepository customerRepository;
    private final TourRepository tourRepository;
    private final TourHelper tourHelper;
    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;
    private final EmailHelper emailHelper;

    @Override
    public TourResponse create(TourRequest request) {
        blackListHelper.isBlackListCustomer(request.getCustomerId());
        var customer = customerRepository.findById(request.getCustomerId()).orElseThrow(()-> new IdNotFoundException(Tables.TOUR.name()));
        var flights = new ArrayList<FlyEntity>();
        request.getFlights().forEach(fly -> flights.add(flyRepository.findById(fly.getId()).orElseThrow(()-> new IdNotFoundException(Tables.FLY.name()))));
        var hotels = new HashMap<HotelEntity, Integer>();
        request.getHotels().forEach(hotel -> hotels.put(this.hotelRepository.findById(hotel.getId()).orElseThrow(), hotel.getTotalDays()));
        var tourToSave = TourEntity.builder()
                .tickets(tourHelper.createTicket(flights, customer))
                .reservations(tourHelper.createReservations(hotels, customer))
                .customer(customer)
                .build();

        var tourSaved = this.tourRepository.save(tourToSave);
        this.customerHelper.increase(customer.getDni(), TourService.class);
        if (Objects.nonNull(request.getEmail())) this.emailHelper.sendMail(request.getEmail(), customer.getFullName(), Tables.RESERVATION.name());
        return TourResponse.builder()
                .reservationsId(tourSaved.getReservations().stream().map(ReservationEntity::getId).collect(Collectors.toList()))
                .ticketsId(tourSaved.getTickets().stream().map(TicketEntity::getId).collect(Collectors.toList()))
                .id(tourSaved.getId())
                .build();
    }

    @Override
    public TourResponse read(Long id) {
        var tourFromDb = tourRepository.findById(id).orElseThrow(()-> new IdNotFoundException(Tables.TOUR.name()));
        return TourResponse.builder()
                .reservationsId(tourFromDb.getReservations().stream().map(ReservationEntity::getId).collect(Collectors.toList()))
                .ticketsId(tourFromDb.getTickets().stream().map(TicketEntity::getId).collect(Collectors.toList()))
                .id(tourFromDb.getId())
                .build();
    }
    @Override
    public void delete(Long id) {
        var tourToDelete = this.tourRepository.findById(id).orElseThrow(()-> new IdNotFoundException(Tables.TOUR.name()));
        this.tourRepository.delete(tourToDelete);
    }
    @Override
    public UUID createTicket(Long flightId, Long tourId) {
        var tourToUpdate = this.tourRepository.findById(tourId).orElseThrow(()-> new IdNotFoundException(Tables.TOUR.name()));
        var fly = this.flyRepository.findById(flightId).orElseThrow(()-> new IdNotFoundException(Tables.FLY.name()));
        var ticket = this.tourHelper.createTicket(fly, tourToUpdate.getCustomer());
        tourToUpdate.addTicket(ticket);
        this.tourRepository.save(tourToUpdate);

        return ticket.getId();
    }
    @Override
    public void deleteTicket(Long tourId, UUID ticketId) {
        var tourToUpdate = this.tourRepository.findById(tourId).orElseThrow(()-> new IdNotFoundException(Tables.TOUR.name()));
        tourToUpdate.removeTicket(ticketId);
        this.tourRepository.save(tourToUpdate);
    }
    @Override
    public UUID createReservation(Long hotelId, Long tourId, Integer totalDays) {
        var tourToUpdate = this.tourRepository.findById(tourId).orElseThrow(()-> new IdNotFoundException(Tables.TOUR.name()));
        var hotel = this.hotelRepository.findById(hotelId).orElseThrow(()-> new IdNotFoundException(Tables.HOTEL.name()));
        var reservation = this.tourHelper.createReservation(hotel, tourToUpdate.getCustomer(), totalDays);
        tourToUpdate.addReservation(reservation);
        this.tourRepository.save(tourToUpdate);

        return reservation.getId();
    }
    @Override
    public void removeReservation(Long tourId, UUID reservationId) {
        var tourToUpdate = this.tourRepository.findById(tourId).orElseThrow(()-> new IdNotFoundException(Tables.TOUR.name()));
        tourToUpdate.removeReservation(reservationId);
        this.tourRepository.save(tourToUpdate);
    }

}
