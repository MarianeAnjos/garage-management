package org.example.garagemanagementapi.controller;

import org.example.garagemanagementapi.dto.WebhookEvent;
import org.example.garagemanagementapi.service.GarageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parked")
public class ParkedController {

    private final GarageService garageService;

    public ParkedController(GarageService garageService) {
        this.garageService = garageService;
    }

    @PostMapping
    public ResponseEntity<?> parkVehicleAtSpot(@RequestBody WebhookEvent event) {
        try {
            String result = garageService.parkVehicle(
                    event.getLicensePlate(), event.getLat(), event.getLng()
            );
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
