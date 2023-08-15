package com.example.best_travel.domain.entities.jpa;

import com.example.best_travel.util.Aeroline;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@Entity(name="fly")
public class FlyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double originLat;
    private Double originLng;
    private Double destinyLat;
    private Double destinyLng;
    @Column(length = 20)
    private String originName;
    @Column(length = 20)
    private String destinyName;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private Aeroline aeroLine;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER, //traer los datos insertados en ticket
            orphanRemoval = true,
            mappedBy = "fly"
    )
    private List<TicketEntity> tickets;

}
