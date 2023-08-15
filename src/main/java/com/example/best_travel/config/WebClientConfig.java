package com.example.best_travel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value(value = "${api.base.url}")
    private String baseUrl;
    @Value(value = "${api.api-key}")
    private String apiKey;
    @Value(value = "${api.api-key.header}")
    private String apiKeyHeader;

    @Bean//(name = "currency")Utilizar cuando ya existe la clase y no se tiene control de ella
    public WebClient currencyWebClient(){
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(apiKeyHeader, apiKey)
                .build();
    }
    //Al tener dos bean de la misma familia o clase se debe nombrar cada bean
//    @Bean(name = "base")
//    public WebClient baseWebClient(){
//        return WebClient.builder()
//                .baseUrl(baseUrl)
//                .defaultHeader(apiKeyHeader, apiKey)
//                .build();
//    }
}
