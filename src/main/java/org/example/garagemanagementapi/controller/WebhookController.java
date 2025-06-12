package org.example.garagemanagementapi.controller;

import org.example.garagemanagementapi.dto.WebhookEvent;
import org.example.garagemanagementapi.model.Spot;
import org.example.garagemanagementapi.model.Vehicle;
import org.example.garagemanagementapi.repository.SpotRepository;
import org.example.garagemanagementapi.repository.VehicleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
public class WebhookController {

    private final VehicleRepository vehicleRepository;
    private final SpotRepository spotRepository;

    public WebhookController(VehicleRepository vehicleRepository, SpotRepository spotRepository) {
        this.vehicleRepository = vehicleRepository;
        this.spotRepository = spotRepository;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody WebhookEvent webhookEvent) {
        String eventType = webhookEvent.getEvent();
        String licensePlate = webhookEvent.getLicensePlate();

        if ("ENTRY".equalsIgnoreCase(eventType)) {
            Vehicle vehicle = new Vehicle();
            vehicle.setLicensePlate(licensePlate);
            vehicle.setEntryTime(LocalDateTime.now());
            vehicleRepository.save(vehicle);
            return new ResponseEntity<>("Vehicle entry recorded", HttpStatus.CREATED);

        } else if ("PARKED".equalsIgnoreCase(eventType)) {
            Optional<Spot> spotOpt = spotRepository.findByLatAndLng(webhookEvent.getLat(), webhookEvent.getLng());
            if (spotOpt.isPresent()) {
                Spot spot = spotOpt.get();
                spot.setLicensePlate(licensePlate);
                spot.setOccupied(true);
                spotRepository.save(spot);
                return new ResponseEntity<>("Vehicle parked at spot " + spot.getId(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Spot not found", HttpStatus.NOT_FOUND);
            }

        } else if ("EXIT".equalsIgnoreCase(eventType)) {
            Optional<Vehicle> vehicleOpt = vehicleRepository.findFirstByLicensePlateAndExitTimeIsNull(licensePlate);
            if (vehicleOpt.isPresent()) {
                Vehicle vehicle = vehicleOpt.get();
                LocalDateTime exitTime = LocalDateTime.now();
                vehicle.setExitTime(exitTime);
                Duration duration = Duration.between(vehicle.getEntryTime(), exitTime);
                long minutes = duration.toMinutes();
                double price = calculatePrice(minutes);
                vehicle.setPrice(price);
                vehicleRepository.save(vehicle);

                Optional<Spot> spotOpt = spotRepository.findByLicensePlate(licensePlate);
                spotOpt.ifPresent(spot -> {
                    spot.setOccupied(false);
                    spot.setLicensePlate("");
                    spotRepository.save(spot);
                });

                return new ResponseEntity<>("Vehicle exited. Total price: R$ " + price, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Vehicle not found", HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<>("Unsupported event type", HttpStatus.BAD_REQUEST);
    }

    private double calculatePrice(long minutes) {
        double hourlyRate = 10.0;
        double hours = Math.ceil(minutes / 60.0);

        long totalSpots = spotRepository.count();
        long freeSpots = spotRepository.findAll().stream().filter(spot -> !spot.getOccupied()).count();
        double occupancyRate = 1.0 - (freeSpots / (double) totalSpots);
        double dynamicFactor = occupancyRate >= 0.8 ? 1.2 : 1.0;

        return hours * hourlyRate * dynamicFactor;
    }
}
