package com.example.best_travel.api.controllers;

import com.example.best_travel.api.models.request.TicketRequest;
import com.example.best_travel.api.models.responses.ErrorResponse;
import com.example.best_travel.api.models.responses.TicketResponse;
import com.example.best_travel.infrastructure.abstract_services.ITicketService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "ticket")
@AllArgsConstructor
@Tag(name = "Ticket")
public class TicketController {
    private final ITicketService ticketService;

    @ApiResponse(
            responseCode = "400",
            description = "When the request has an invalid field, we response this",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }
    )
    @PostMapping
    public ResponseEntity<TicketResponse> post(@RequestBody TicketRequest request) {
        return ResponseEntity.ok(ticketService.create(request));
    }

    //Id del parametro del metodo tiene que ser el mismo nombre que se le pone al parametro de la URL
    //sino se tiene que especificar
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> get(@PathVariable UUID id) {
        var authorities = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authorities.getAuthorities());
        return ResponseEntity.ok(ticketService.read(id));

    }
    @GetMapping("/")
    public ResponseEntity<Map<String, BigDecimal>> getFlyPrice(@RequestParam Long flyId) {
        return ResponseEntity.ok(Collections.singletonMap("flyPrice", this.ticketService.findPrice(flyId)));

    }
    @PutMapping("/{id}")
    public ResponseEntity<TicketResponse> put(@PathVariable UUID id, @RequestBody TicketRequest request) {
        return ResponseEntity.ok(this.ticketService.update(request, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }
}