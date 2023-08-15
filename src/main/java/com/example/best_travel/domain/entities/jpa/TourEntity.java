package com.example.best_travel.domain.entities.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@Entity(name = "tour")
public class TourEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER, //traer los datos insertados en ticket
            orphanRemoval = true,
            mappedBy = "tour"
    )
    private List<ReservationEntity> reservations;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER, //traer los datos insertados en ticket
            orphanRemoval = true,
            mappedBy = "tour"
    )
    private List<TicketEntity> tickets;
    @ManyToOne
    @JoinColumn(name = "id_customer")
    private CustomerEntity customer;

    @PrePersist
    @PreRemove
    public void updateFK(){
        this.reservations.forEach( r -> r.setTour(this));
        this.tickets.forEach( t -> t.setTour(this));
    }

    public void removeTicket(UUID id){
        this.tickets.forEach(ticket-> {
            if (ticket.getId().equals(id))
                ticket.setTour(null);
        });
    }

    public void addTicket(TicketEntity ticket){
        if (Objects.isNull(ticket)) this.tickets = new ArrayList<>();
        this.tickets.add(ticket);
        this.tickets.forEach( t -> t.setTour(this));
    }
    public void removeReservation(UUID id){
        this.reservations.forEach(reservation-> {
            if (reservation.getId().equals(id))
                reservation.setTour(null);
        });
    }

        public void addReservation(ReservationEntity reservation){
        if (Objects.isNull(reservation)) this.reservations = new ArrayList<>();
        this.reservations.add(reservation);
        this.reservations.forEach( t -> t.setTour(this));
    }
}
