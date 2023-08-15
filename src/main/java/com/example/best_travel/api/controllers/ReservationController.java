package com.example.best_travel.api.controllers;

import com.example.best_travel.api.models.request.ReservationRequest;
import com.example.best_travel.api.models.request.TicketRequest;
import com.example.best_travel.api.models.responses.ErrorResponse;
import com.example.best_travel.api.models.responses.ReservationResponse;
import com.example.best_travel.api.models.responses.TicketResponse;
import com.example.best_travel.infrastructure.abstract_services.IReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping(path = "reservation")
@AllArgsConstructor
@Tag(name = "Reservation")
public class ReservationController {
    private final IReservationService reservationService;

    @ApiResponse(
            responseCode = "400",
            description = "When the request has an invalid field, we response this",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }
    )
    @PostMapping
    public ResponseEntity<ReservationResponse> post(@Valid @RequestBody ReservationRequest request){
        return ResponseEntity.ok(reservationService.create(request));
    }

    //Id del parametro del metodo tiene que ser el mismo nombre que se le pone al parametro de la URL
    //sino se tiene que especificar
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(reservationService.read(id));

    }
    @Operation(summary = "Returning a reservation price based on a hotel id")
    @GetMapping("/")
    public ResponseEntity<Map<String, BigDecimal>> getReservationPrice(@RequestParam Long hotelId,
                                                                       @RequestHeader(required = false) String currency) {
        if (Objects.isNull(currency)) currency = String.valueOf(Currency.getInstance("USD"));
        return ResponseEntity.ok(Collections.singletonMap("ticketPrice", this.reservationService.findPrice(hotelId, currency)));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponse> put(@Valid @PathVariable UUID id, @RequestBody ReservationRequest request) {
        return ResponseEntity.ok(this.reservationService.update(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
