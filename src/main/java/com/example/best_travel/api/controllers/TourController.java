package com.example.best_travel.api.controllers;

import com.example.best_travel.api.models.request.TourRequest;
import com.example.best_travel.api.models.responses.ErrorResponse;
import com.example.best_travel.api.models.responses.TourResponse;
import com.example.best_travel.infrastructure.abstract_services.service.TourService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "tour")
@AllArgsConstructor
@Tag(name = "Tour")
public class TourController {
    private final TourService tourService;

    @ApiResponse(
            responseCode = "400",
            description = "When the request has an invalid field, we response this",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }
    )
    @Operation(summary = "Saving a tour in system based on a list of hotels and flights")
    @PostMapping("/")
    public ResponseEntity<TourResponse> post(@Valid @RequestBody TourRequest request){
        return ResponseEntity.ok(tourService.create(request));
    }

    @Operation(summary = "Returning a tour with its id")
    @GetMapping("/{id}")
    public ResponseEntity<TourResponse> getAll(@PathVariable Long id){
        return ResponseEntity.ok(tourService.read(id));
    }

    @Operation(summary = "Removing a tour")
    @DeleteMapping("/{id}")
    public ResponseEntity<TourResponse> delete(@PathVariable Long id){
        tourService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Removing a ticket from a tour")
    @PatchMapping("/{tourId}/remove_ticket/{ticketId}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long tourId, @PathVariable UUID ticketId){
        tourService.deleteTicket(tourId,ticketId);
        return ResponseEntity.noContent().build();
    }

    /**
     * crear tkt a partir de un vuelo
     * @param tourId
     * @param flyId
     * @return
     */
    @PatchMapping("/{tourId}/add_ticket/{flyId}")
    public ResponseEntity<Map<String, UUID>> postTicket(@PathVariable Long tourId, @PathVariable Long flyId){
        var response= Collections.singletonMap("ticketId", tourService.createTicket(flyId, tourId));
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Removing a reservation from a tour")
    @PatchMapping("/{tourId}/remove_reservation/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long tourId, @PathVariable UUID reservationId){
        tourService.removeReservation(tourId,reservationId);
        return ResponseEntity.noContent().build();
    }

    /**
     * crear tkt a partir de una reservation
     * @param tourId
     * @param hotelId
     * @return
     */
    @Operation(summary = "Adding a reservation from a tour")
    @PatchMapping("/{tourId}/add_reservation/{hotelId}")
    public ResponseEntity<Map<String, UUID>> postReservation(@PathVariable Long tourId, @PathVariable Long hotelId, @RequestParam Integer totalDays){
        var response= Collections.singletonMap("ticketId", tourService.createReservation(hotelId, tourId, totalDays));
        return ResponseEntity.ok(response);
    }
}
