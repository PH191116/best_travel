package com.example.best_travel.domain.entities.jpa;

import jakarta.persistence.*;
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
@Entity(name = "ticket")
public class TicketEntity {
    @Id
    private UUID id;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
    private LocalDate purchaseDate;
    private BigDecimal price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fly_id")
    private FlyEntity fly;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id", nullable = true)
    private TourEntity tour;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = true)
    private CustomerEntity customer;
}
