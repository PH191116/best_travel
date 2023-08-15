package com.example.best_travel.infrastructure.abstract_services.service;

import com.example.best_travel.api.models.request.TicketRequest;
import com.example.best_travel.api.models.responses.FlyResponse;
import com.example.best_travel.api.models.responses.TicketResponse;
import com.example.best_travel.domain.entities.jpa.TicketEntity;
import com.example.best_travel.domain.repositories.CustomerRepository;
import com.example.best_travel.domain.repositories.FlyRepository;
import com.example.best_travel.domain.repositories.TicketRepository;
import com.example.best_travel.infrastructure.abstract_services.ITicketService;
import com.example.best_travel.infrastructure.abstract_services.helpers.BlackListHelper;
import com.example.best_travel.infrastructure.abstract_services.helpers.CustomerHelper;
import com.example.best_travel.infrastructure.abstract_services.helpers.EmailHelper;
import com.example.best_travel.util.BestTravelUtil;
import com.example.best_travel.util.Tables;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class TicketService implements ITicketService {

    private final FlyRepository flyRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;
    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;
    private final EmailHelper emailHelper;

    @Override
    public TicketResponse create(TicketRequest request) {
        blackListHelper.isBlackListCustomer(request.getIdClient());
        var fly = flyRepository.findById(request.getIdFlight()).orElseThrow();
        var customer = customerRepository.findById(request.getIdClient()).orElseThrow();
        customerHelper.increase(customer.getDni(), TicketService.class);
        var ticketToPersist = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().add(fly.getPrice().multiply(CHARGER_PRICE_PERCENTAGE)))
                .purchaseDate(LocalDate.now())
                .arrivalDate(BestTravelUtil.getRandomLater())
                .departureDate(BestTravelUtil.getRandomSoon())
                .build();
        var ticketPersisted = this.ticketRepository.save(ticketToPersist);
        log.info("Ticket saved with id: {}", ticketPersisted.getId());
        if (Objects.nonNull(request.getEmail())) this.emailHelper.sendMail(request.getEmail(), customer.getFullName(), Tables.TICKET.name());

        return this.entityToResponse(ticketPersisted);
    }

    @Override
    public TicketResponse read(UUID uuid) {
        var tkt = this.ticketRepository.findById(uuid).orElseThrow();
        return this.entityToResponse(tkt);
    }

    @Override
    public TicketResponse update(TicketRequest request, UUID uuid) {
        var ticketToUpdate = ticketRepository.findById(uuid).orElseThrow();
        var fly = flyRepository.findById(request.getIdFlight()).orElseThrow();

        ticketToUpdate.setFly(fly);
        ticketToUpdate.setPrice(fly.getPrice().add(fly.getPrice().multiply(CHARGER_PRICE_PERCENTAGE)));
        ticketToUpdate.setDepartureDate(BestTravelUtil.getRandomSoon());
        ticketToUpdate.setArrivalDate(BestTravelUtil.getRandomLater());

        var ticketUpdated = this.ticketRepository.save(ticketToUpdate);
        log.info("Ticket updated with id{}", ticketUpdated.getId());
        return this.entityToResponse(ticketUpdated);
    }

    @Override
    public void delete(UUID uuid) {
        var ticketToDelete = ticketRepository.findById(uuid).orElseThrow();
        this.ticketRepository.delete(ticketToDelete);
    }

    @Override
    public BigDecimal findPrice(Long idFly) {
        var fly = flyRepository.findById(idFly).orElseThrow();
        return fly.getPrice().add(fly.getPrice().multiply(CHARGER_PRICE_PERCENTAGE));
    }

    /**
     * Mapeo entre entidades y respuesta
     * @param entity
     * @return
     */
    private TicketResponse entityToResponse(TicketEntity entity){
        var response = new TicketResponse();
        BeanUtils.copyProperties(entity, response);
        var flyResponse = new FlyResponse();
        BeanUtils.copyProperties(entity.getFly(), flyResponse);
        response.setFly(flyResponse);
        return response;
    }

    public static final BigDecimal CHARGER_PRICE_PERCENTAGE = BigDecimal.valueOf(0.25);

}
