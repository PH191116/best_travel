package com.example.best_travel.infrastructure.abstract_services.helpers;

import com.example.best_travel.infrastructure.abstract_services.dto.CurrencyDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Currency;

@Component
/**
 * No se utiliza AllArgsConstructor de Lombok porque no es compatible con
 * la anotacion @Value, se causaria un problema de inyeccion de beans
 */
public class ApiCurrencyConnectorHelper {
    private final WebClient webClient;
    @Value(value = "${api.api-key}")
    private String baseCurrency;
    private static final String BASE_CURRENCY_QUERY_PARAM="?base={base}";
    private static final String SYMBOL_CURRENCY_QUERY_PARAM="&symbols={symbol}";
    public ApiCurrencyConnectorHelper(WebClient webClient) {
        this.webClient = webClient;
    }
    public CurrencyDTO getCurrency(String currency){
        return this.webClient
                .get()
                .uri(uri ->
                        uri.query(BASE_CURRENCY_QUERY_PARAM)
                                  .query(SYMBOL_CURRENCY_QUERY_PARAM)
                                  .build(baseCurrency,currency))
                .retrieve()//Ejecutando uri
                .bodyToMono(CurrencyDTO.class)//Flux para collections y Mono para single data
                .block();//Block cuando se usa spring normal, de lo contrario con spring reactivo se utiliza callbacks
    }
}
