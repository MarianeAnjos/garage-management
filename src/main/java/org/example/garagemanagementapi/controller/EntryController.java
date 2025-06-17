package org.example.garagemanagementapi.controller;

import jakarta.validation.Valid;
import org.example.garagemanagementapi.dto.WebhookEvent;
import org.example.garagemanagementapi.service.GarageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entry")
public class EntryController {

    private final GarageService garageService;

    public EntryController(GarageService garageService) {
        this.garageService = garageService;
    }

    @PostMapping
    public ResponseEntity<?> handleEntry(@Valid @RequestBody WebhookEvent event) {
        try {
            garageService.registerEntry(
                    event.getLicensePlate(), event.getLat(), event.getLng()
            );
            return ResponseEntity.status(201).body("Entrada registrada com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}