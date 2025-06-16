package org.example.garagemanagementapi.controller;

import jakarta.validation.Valid;
import org.example.garagemanagementapi.dto.WebhookEvent;
import org.example.garagemanagementapi.service.GarageService;
import org.springframework.http.HttpStatus;
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
            String result = garageService.registerEntry(
                    event.getLicensePlate(), event.getLat(), event.getLng()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
