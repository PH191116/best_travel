package com.example.best_travel.api.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class TicketRequest {

    @NotBlank
    private String idClient;
    @Positive
    private Long idFlight;
    @Email
    private String email;
}
