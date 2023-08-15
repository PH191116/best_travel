package com.example.best_travel.infrastructure.abstract_services.helpers;

import com.example.best_travel.domain.entities.jpa.*;
import com.example.best_travel.domain.repositories.ReservationRepository;
import com.example.best_travel.domain.repositories.TicketRepository;
import com.example.best_travel.infrastructure.abstract_services.service.ReservationService;
import com.example.best_travel.infrastructure.abstract_services.service.TicketService;
import com.example.best_travel.util.BestTravelUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Clase encargada de la creacion de tickets y reservaciones
 * unicamente para tours
 */
@Transactional
@Component
@AllArgsConstructor
public class TourHelper {
    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;

    public List<TicketEntity> createTicket(List<FlyEntity> flights, CustomerEntity customer){
        //Se inicializa con el tamaño de flights porque sera un tkt por cada vuelo
        var response = new ArrayList<TicketEntity>(flights.size());
        flights.forEach(fly -> {
            var ticketToPersist = TicketEntity.builder()
                    .id(UUID.randomUUID())
                    .fly(fly)
                    .customer(customer)
                    .price(fly.getPrice().add(fly.getPrice().multiply(TicketService.CHARGER_PRICE_PERCENTAGE)))
                    .purchaseDate(LocalDate.now())
                    .arrivalDate(BestTravelUtil.getRandomLater())
                    .departureDate(BestTravelUtil.getRandomSoon())
                    .build();
            response.add(this.ticketRepository.save(ticketToPersist));
        });
        return response;
    }

    public List<ReservationEntity> createReservations(HashMap<HotelEntity, Integer> hotels, CustomerEntity customer){
        //Se inicializa con el tamaño de flights porque sera un tkt por cada vuelo
        var response = new ArrayList<ReservationEntity>(hotels.size());
        hotels.forEach((hotel, totalDays)->{
            var reservationToSave = ReservationEntity.builder()
                    .id(UUID.randomUUID())
                    .hotel(hotel)
                    .customer(customer)
                    .price(hotel.getPrice().add(hotel.getPrice().multiply(ReservationService.CHARGER_PRICE_PERCENTAGE)))
                    .dateStart(LocalDate.now())
                    .dateEnd(LocalDate.now().plusDays(totalDays))
                    .totalDays(totalDays)
                    .dateTimeReservation(LocalDateTime.now())
                    .build();
            response.add(this.reservationRepository.save(reservationToSave));
        });
        return response;
    }

    public TicketEntity createTicket(FlyEntity fly, CustomerEntity customer){
        var ticketToPersist = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().add(fly.getPrice().multiply(TicketService.CHARGER_PRICE_PERCENTAGE)))
                .purchaseDate(LocalDate.now())
                .arrivalDate(BestTravelUtil.getRandomLater())
                .departureDate(BestTravelUtil.getRandomSoon())
                .build();
        return this.ticketRepository.save(ticketToPersist);
    }

    public ReservationEntity createReservation(HotelEntity hotel, CustomerEntity customer, Integer totalDays){
        var reservationToSave = ReservationEntity.builder()
                .id(UUID.randomUUID())
                .hotel(hotel)
                .customer(customer)
                .price(hotel.getPrice().add(hotel.getPrice().multiply(ReservationService.CHARGER_PRICE_PERCENTAGE)))
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(totalDays))
                .totalDays(totalDays)
                .dateTimeReservation(LocalDateTime.now())
                .build();
        return this.reservationRepository.save(reservationToSave);
    }
}
