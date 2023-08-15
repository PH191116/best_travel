package com.example.best_travel.domain.entities.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@Entity(name = "customer")
public class CustomerEntity {

    @Id
    @Column(length = 20)
    private String dni;
    @Column(length = 50)
    private String fullName;
    @Column(length = 20)
    private String creditCard;
    @Column(length = 12)
    private String phoneNumber;
    private Integer totalFlights;
    private Integer totalLodgings;
    private Integer totalTours;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER, //traer los datos insertados en ticket
            orphanRemoval = true,
            mappedBy = "customer"
    )
    private List<TicketEntity> tickets;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER, //traer los datos insertados en reservaciones
            orphanRemoval = true,
            mappedBy = "customer"
    )
    private List<ReservationEntity> reservations;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, //traer los datos insertados en tours
            orphanRemoval = true,
            mappedBy = "customer"
    )
    private List<TourEntity> tours;
}
