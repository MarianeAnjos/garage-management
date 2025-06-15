package org.example.garagemanagementapi.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.example.garagemanagementapi.dto.WebhookEvent;
import org.example.garagemanagementapi.model.Spot;
import org.example.garagemanagementapi.repository.SpotRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/parked")
public class ParkedController {

    private final SpotRepository spotRepository;

    public ParkedController(SpotRepository spotRepository) {
        this.spotRepository = spotRepository;
    }

    @PostMapping
    public ResponseEntity<?> handleParked(@RequestBody WebhookEvent event) {
        // Aqui, o atributo event.getLat() e event.getLng() serão usados para buscar o spot.
        Optional<Spot> spotOpt = spotRepository.findByLatAndLng(event.getLat(), event.getLng());
        if (spotOpt.isPresent()) {
            Spot spot = spotOpt.get();
            spot.setLicensePlate(event.getLicensePlate());
            spot.setOccupied(true);
            spotRepository.save(spot);
            return ResponseEntity.ok("Vehicle parked at spot " + spot.getId());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Spot not found");
        }
    }
}
