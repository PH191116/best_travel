package com.example.best_travel.api.models.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class TicketResponse {
    private UUID id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departureDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrivalDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;
    private BigDecimal price;
    private FlyResponse fly;
}
