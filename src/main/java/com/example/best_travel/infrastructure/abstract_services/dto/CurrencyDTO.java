package com.example.best_travel.infrastructure.abstract_services.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Map;

@Data
public class CurrencyDTO {
    @JsonProperty(value= "date")//Anotacion para poder mapear con diferente nombre el nombre de la propiedad de la api externa
    private LocalDate exchangeDate;
    private Map<String, BigDecimal> rates;
}
