package org.example.garagemanagementapi.controller;

import org.example.garagemanagementapi.dto.WebhookEvent;
import org.example.garagemanagementapi.service.GarageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private final GarageService garageService;

    public WebhookController(GarageService garageService) {
        this.garageService = garageService;
    }

    @PostMapping
    public ResponseEntity<?> handleWebhook(@RequestBody WebhookEvent event) {
        if (event.getEvent() == null || event.getLicensePlate() == null) {
            return ResponseEntity.badRequest().body("Evento e placa são obrigatórios.");
        }

        try {
            return switch (event.getEvent().toUpperCase()) {
                case "ENTRY" -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(garageService.registerEntry(event.getLicensePlate(), event.getLat(), event.getLng()));
                case "PARKED" -> ResponseEntity.ok(garageService.parkVehicle(event.getLicensePlate(), event.getLat(), event.getLng()));
                case "EXIT" -> ResponseEntity.ok(garageService.registerExit(event.getLicensePlate()));
                default -> ResponseEntity.badRequest().body("Tipo de evento não suportado.");
            };
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}