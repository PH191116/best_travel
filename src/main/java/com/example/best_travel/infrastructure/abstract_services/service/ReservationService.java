package com.example.best_travel.infrastructure.abstract_services.service;

import com.example.best_travel.api.models.request.ReservationRequest;
import com.example.best_travel.api.models.responses.HotelResponse;
import com.example.best_travel.api.models.responses.ReservationResponse;
import com.example.best_travel.domain.entities.jpa.ReservationEntity;
import com.example.best_travel.domain.repositories.CustomerRepository;
import com.example.best_travel.domain.repositories.HotelRepository;
import com.example.best_travel.domain.repositories.ReservationRepository;
import com.example.best_travel.infrastructure.abstract_services.IReservationService;
import com.example.best_travel.infrastructure.abstract_services.helpers.ApiCurrencyConnectorHelper;
import com.example.best_travel.infrastructure.abstract_services.helpers.BlackListHelper;
import com.example.best_travel.infrastructure.abstract_services.helpers.CustomerHelper;
import com.example.best_travel.infrastructure.abstract_services.helpers.EmailHelper;
import com.example.best_travel.util.Tables;
import com.example.best_travel.util.exceptions.IdNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class ReservationService implements IReservationService {
    private final HotelRepository hotelRepository;
    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;
    private final ApiCurrencyConnectorHelper apiCurrencyConnectorHelper;
    private final EmailHelper emailHelper;
    @Override
    public ReservationResponse create(ReservationRequest request) {
        blackListHelper.isBlackListCustomer(request.getIdClient());
        var hotel = hotelRepository.findById(request.getIdHotel()).orElseThrow(()-> new IdNotFoundException("hotel"));
        var customer = customerRepository.findById(request.getIdClient()).orElseThrow(()-> new IdNotFoundException("customer"));
        customerHelper.increase(customer.getDni(), ReservationService.class);
        var reservationToSave = ReservationEntity.builder()
                .id(UUID.randomUUID())
                .hotel(hotel)
                .customer(customer)
                .price(hotel.getPrice().add(hotel.getPrice().multiply(CHARGER_PRICE_PERCENTAGE)))
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(request.getTotalDays()))
                .totalDays(request.getTotalDays())
                .dateTimeReservation(LocalDateTime.now())
                .build();
        var reservationSaved = this.reservationRepository.save(reservationToSave);
        log.info("Reservation saved with id: {}", reservationSaved.getId());
        if (Objects.nonNull(request.getEmail())) this.emailHelper.sendMail(request.getEmail(), customer.getFullName(), Tables.RESERVATION.name());
        return this.reservationToResponse(reservationSaved);
    }

    @Override
    public ReservationResponse read(UUID uuid) {
        var reservation = this.reservationRepository.findById(uuid).orElseThrow(()-> new IdNotFoundException("reservation"));
        return this.reservationToResponse(reservation);    }

    @Override
    public ReservationResponse update(ReservationRequest request, UUID uuid) {
        var hotel = hotelRepository.findById(request.getIdHotel()).orElseThrow(()-> new IdNotFoundException("hotel"));
        var reservationToUpdate = this.reservationRepository.findById(uuid).orElseThrow(()-> new IdNotFoundException("reservation"));
        reservationToUpdate.setHotel(hotel);
        reservationToUpdate.setTotalDays(request.getTotalDays());
        reservationToUpdate.setDateStart(LocalDate.now());
        reservationToUpdate.setDateEnd(LocalDate.now().plusDays(request.getTotalDays()));
        reservationToUpdate.setPrice(hotel.getPrice().add(hotel.getPrice().multiply(CHARGER_PRICE_PERCENTAGE)));

        var reservationUpdated = this.reservationRepository.save(reservationToUpdate);

        log.info("Reservation updated with id{}", reservationToUpdate.getId());

        return reservationToResponse(reservationUpdated);
    }
    @Override
    public BigDecimal findPrice(Long hotelId, String currency) {
        var hotel = hotelRepository.findById(hotelId).orElseThrow();
        var priceInUsd = hotel.getPrice().add(hotel.getPrice().multiply(CHARGER_PRICE_PERCENTAGE));
        if (currency.equals("USD")) return priceInUsd;
        var currencyDTO = this.apiCurrencyConnectorHelper.getCurrency(currency);
        log.info("API currency in: "+currencyDTO.getExchangeDate().toString()+", response: "+currencyDTO.getRates());
        return priceInUsd.multiply(currencyDTO.getRates().get(currency));
    }
    @Override
    public void delete(UUID uuid) {
        var reservationDelete = reservationRepository.findById(uuid).orElseThrow(()-> new IdNotFoundException("reservation"));
        this.reservationRepository.delete(reservationDelete);
    }

    /**
     * Mapeo entre entidades y respuesta
     * @param entity
     * @return
     */

    private ReservationResponse reservationToResponse(ReservationEntity entity){
        var response = new ReservationResponse();
        BeanUtils.copyProperties(entity, response);
        var hotelResponse = new HotelResponse();
        BeanUtils.copyProperties(entity.getHotel(), hotelResponse);
        response.setHotel(hotelResponse);
        return response;
    }

    public static final BigDecimal CHARGER_PRICE_PERCENTAGE = BigDecimal.valueOf(0.20);
}
