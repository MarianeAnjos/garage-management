package org.example.garagemanagementapi.controller;

import org.example.garagemanagementapi.model.Spot;
import org.example.garagemanagementapi.repository.SpotRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/spots")
public class SpotController {

    private final SpotRepository spotRepository;

    public SpotController(SpotRepository spotRepository) {
        this.spotRepository = spotRepository;
    }

    @GetMapping
    public List<Spot> getAllSpots() {
        return spotRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Spot> getSpotById(@PathVariable Long id) {
        return spotRepository.findById(id)
                .map(spot -> ResponseEntity.ok(spot))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Spot> createSpot(@RequestBody Spot spot) {
        spot.setId(null); // Garante que não sobrescreve uma vaga existente
        Spot novoSpot = spotRepository.save(spot);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoSpot);
    }

    @PostMapping("/spot-status")
    public ResponseEntity<Spot> getSpotStatus(@RequestBody Map<String, Double> coord) {
        Double lat = coord.get("lat");
        Double lng = coord.get("lng");

        if (lat == null || lng == null) {
            return ResponseEntity.badRequest().build(); // Ou retornar 400 com DTO separado, se quiser
        }

        return spotRepository.findByLatAndLng(lat, lng)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Spot> updateSpot(@PathVariable Long id, @RequestBody Spot spotDetails) {
        return spotRepository.findById(id).map(spot -> {
            spot.setLat(spotDetails.getLat());
            spot.setLng(spotDetails.getLng());
            spot.setLicensePlate(spotDetails.getLicensePlate());
            spot.setOccupied(spotDetails.isOccupied());
            Spot updated = spotRepository.save(spot);
            return ResponseEntity.ok(updated);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpot(@PathVariable Long id) {
        if (spotRepository.existsById(id)) {
            spotRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
