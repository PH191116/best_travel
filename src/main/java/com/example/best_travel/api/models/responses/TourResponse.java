package com.example.best_travel.api.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class TourResponse {
    private Long id;
    private List<UUID> ticketsId;
    private List<UUID> reservationsId;
}
