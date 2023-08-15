package com.example.best_travel.infrastructure.abstract_services.service;

import com.example.best_travel.api.models.responses.HotelResponse;
import com.example.best_travel.domain.entities.jpa.HotelEntity;
import com.example.best_travel.domain.repositories.HotelRepository;
import com.example.best_travel.infrastructure.abstract_services.IHotelService;
import com.example.best_travel.util.SortType;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@AllArgsConstructor
public class HotelService implements IHotelService {
    private final HotelRepository hotelRepository;
    @Override
    public Page<HotelResponse> readAll(Integer page, Integer size, SortType sortType) {
        //Iniciar como nulo por eso se crea el objeto de este tipo,
        // ya que es el tipo de objeto recibe spring
        PageRequest pageRequest = null;
        switch (sortType){
            case NONE -> pageRequest = PageRequest.of(page, size);
            case LOWER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).ascending());
            case UPPER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).descending());
        }
        return this.hotelRepository.findAll(pageRequest).map(this::entityToResponse);
    }

    @Override
    public List<HotelResponse> readLessPrice(BigDecimal price) {
        return this.hotelRepository.findByPriceLessThan(price)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<HotelResponse> readBetweenPrice(BigDecimal min, BigDecimal max) {
        return this.hotelRepository.findByPriceIsBetween(min, max)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toList());    }

    @Override
    public List<HotelResponse> readByRating(Integer rating) {
        return this.hotelRepository.findByRatingGreaterThan(rating)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toList());    }

    private HotelResponse entityToResponse(HotelEntity entity){
        HotelResponse response = new HotelResponse();
        //Copy the property values of the given source bean into the target bean.
        BeanUtils.copyProperties(entity, response);
        return response;
    }
}
