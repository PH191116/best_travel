package com.example.best_travel.domain.repositories;

import com.example.best_travel.domain.entities.jpa.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HotelRepository extends JpaRepository<HotelEntity, Long> {
    List<HotelEntity> findByPriceLessThan(BigDecimal price);
    List<HotelEntity> findByPriceIsBetween(BigDecimal min, BigDecimal max);
    List<HotelEntity> findByRatingGreaterThan(Integer rating);
    Optional<HotelEntity> findByReservationsId(UUID id);
}
