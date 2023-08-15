package com.example.best_travel.api.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class TourRequest {

    @Positive
    @NotNull(message = "Id customer is mandatory")
    private String customerId;
    @Size(min=1,message = "Min of flights must be 1 ")
    private List<TourFlyRequest> flights;
    @Size(min=1,message = "Min of hotels must be 1 ")
    private List<TourHotelRequest> hotels;
    @Email
    private String email;
}
