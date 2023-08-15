package com.example.best_travel.domain.repositories;

import com.example.best_travel.domain.entities.jpa.FlyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FlyRepository extends JpaRepository<FlyEntity, Long> {

    //@Query(value = "SELECT f FROM fly f WHERE f.price < :price", nativeQuery = true)
    @Query("SELECT f FROM fly f WHERE f.price < :price")
    List<FlyEntity> selectLessPrice(BigDecimal price);
    @Query("SELECT f FROM fly f WHERE f.price BETWEEN :min AND :max")
    List<FlyEntity> selectBetweenPrice(BigDecimal min, BigDecimal max);
    @Query("SELECT f FROM fly f WHERE f.originName=:origin AND f.destinyName=:destiny")
    List<FlyEntity> selectOriginDestiny(String origin, String destiny);

    /**
     * Join sin JPQL
     */
    @Query("SELECT f FROM fly f join fetch f.tickets t where t.id =:id")
    Optional<FlyEntity> findByTicketId(UUID id);
}
