package com.example.best_travel.infrastructure.abstract_services;

import com.example.best_travel.api.models.request.TicketRequest;
import com.example.best_travel.api.models.responses.TicketResponse;

import java.math.BigDecimal;
import java.util.UUID;

public interface ITicketService extends CrudService<TicketRequest, TicketResponse, UUID> {

    BigDecimal findPrice(Long idFly);
}
