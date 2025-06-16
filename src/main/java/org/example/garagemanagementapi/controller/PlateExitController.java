package org.example.garagemanagementapi.controller;

import org.example.garagemanagementapi.dto.WebhookEvent;
import org.example.garagemanagementapi.model.Spot;
import org.example.garagemanagementapi.model.Vehicle;
import org.example.garagemanagementapi.repository.SpotRepository;
import org.example.garagemanagementapi.repository.VehicleRepository;
import org.example.garagemanagementapi.service.GarageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/plate-exit")
public class PlateExitController {

    private final GarageService garageService;

    public PlateExitController(GarageService garageService) {
        this.garageService = garageService;
    }

    @PostMapping
    public ResponseEntity<String> handleExit(@RequestBody WebhookEvent event) {
        if (event.getLicensePlate() == null || event.getLicensePlate().isBlank()) {
            return ResponseEntity.badRequest().body("Placa do veículo é obrigatória.");
        }

        String result = garageService.registerExit(event.getLicensePlate());

        if (result.toLowerCase().contains("not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        return ResponseEntity.ok(result);
    }

}
