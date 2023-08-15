package com.example.best_travel.api.models.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class TourHotelRequest {
    private Long id;
    private Integer totalDays;
}
