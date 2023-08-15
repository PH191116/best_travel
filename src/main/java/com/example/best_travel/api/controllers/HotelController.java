package com.example.best_travel.api.controllers;

import com.example.best_travel.api.models.responses.FlyResponse;
import com.example.best_travel.api.models.responses.HotelResponse;
import com.example.best_travel.infrastructure.abstract_services.IFlyService;
import com.example.best_travel.infrastructure.abstract_services.IHotelService;
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
@RequestMapping(path = "hotel")
@AllArgsConstructor
@Tag(name = "Hotel")
public class HotelController {
    private final IHotelService hotelService;

    @GetMapping("/")
    @Notify
        public ResponseEntity<Page<HotelResponse>> getAll(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestHeader(required = false) SortType sortType){
            if (Objects.isNull(sortType)) sortType = SortType.NONE;
            var response = hotelService.readAll(page, size, sortType);
            return response.isEmpty()?ResponseEntity.noContent().build():ResponseEntity.ok(response);
    }
    @GetMapping("/less_price")
    public ResponseEntity<List<HotelResponse>> getLessPrice(@RequestParam BigDecimal price){
        var response = hotelService.readLessPrice(price);
        return response.isEmpty()?ResponseEntity.noContent().build():ResponseEntity.ok(response);
    }
    @GetMapping("/between_price")
    public ResponseEntity<List<HotelResponse>> getBetweenPrice(@RequestParam BigDecimal min, @RequestParam BigDecimal max){
        var response = hotelService.readBetweenPrice(min, max);
        return response.isEmpty()?ResponseEntity.noContent().build():ResponseEntity.ok(response);
    }
    @GetMapping("/rating")
    public ResponseEntity<List<HotelResponse>> getByRating(@RequestParam Integer rating){
        if (rating > 4) rating = 4;
        if (rating < 1) rating = 1;
        var response = hotelService.readByRating(rating);
        return response.isEmpty()?ResponseEntity.noContent().build():ResponseEntity.ok(response);
    }
}
