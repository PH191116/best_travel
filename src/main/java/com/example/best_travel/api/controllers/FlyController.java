package com.example.best_travel.api.controllers;

import com.example.best_travel.api.models.responses.FlyResponse;
import com.example.best_travel.infrastructure.abstract_services.IFlyService;
import com.example.best_travel.util.SortType;
import com.example.best_travel.util.annotations.Notify;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "fly")
@AllArgsConstructor
@Tag(name = "Fly")
public class FlyController {
    private final IFlyService flyService;

    @GetMapping("/")
    @Notify
        public ResponseEntity<Page<FlyResponse>> getAll(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestHeader(required = false) SortType sortType){
            if (Objects.isNull(sortType)) sortType = SortType.NONE;
            var response = flyService.readAll(page, size, sortType);
            return response.isEmpty()?ResponseEntity.noContent().build():ResponseEntity.ok(response);
    }
    @GetMapping("/less_price")
    public ResponseEntity<List<FlyResponse>> getLessPrice(@RequestParam BigDecimal price){
        var response = flyService.readLessPrice(price);
        return response.isEmpty()?ResponseEntity.noContent().build():ResponseEntity.ok(response);
    }
    @GetMapping("/between_price")
    public ResponseEntity<List<FlyResponse>> getBetweenPrice(@RequestParam BigDecimal min, @RequestParam BigDecimal max){
        var response = flyService.readBetweenPrice(min, max);
        return response.isEmpty()?ResponseEntity.noContent().build():ResponseEntity.ok(response);
    }
    @GetMapping("/origin_destiny")
    public ResponseEntity<List<FlyResponse>> getOriginDestiny(@RequestParam String origin, @RequestParam String destiny){
        var response = flyService.readByOriginDestiny(origin, destiny);
        return response.isEmpty()?ResponseEntity.noContent().build():ResponseEntity.ok(response);
    }
}
