package com.example.best_travel.api.models.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class ReservationRequest {
    @Size(min = 18, max = 20, message = "The size needs to be between 18 and 20 characters")
    @NotBlank(message = "Field cannot be empty")
    private String idClient;
    @Positive
    @NotNull(message = "Id hotel is mandatory")
    private Long idHotel;
    @Min(value = 1)
    @Max(value = 30)
    @NotNull(message = "Total days is mandatory")
    private Integer totalDays;
    //@Pattern(regexp = ")
    @Email
    private String email;
}
