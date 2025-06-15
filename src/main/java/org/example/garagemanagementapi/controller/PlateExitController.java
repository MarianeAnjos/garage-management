package org.example.garagemanagementapi.controller;

import org.example.garagemanagementapi.dto.WebhookEvent;
import org.example.garagemanagementapi.model.Spot;
import org.example.garagemanagementapi.model.Vehicle;
import org.example.garagemanagementapi.repository.SpotRepository;
import org.example.garagemanagementapi.repository.VehicleRepository;
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

    private final VehicleRepository vehicleRepository;
    private final SpotRepository spotRepository;

    public PlateExitController(VehicleRepository vehicleRepository, SpotRepository spotRepository) {
        this.vehicleRepository = vehicleRepository;
        this.spotRepository = spotRepository;
    }

    @PostMapping
    public ResponseEntity<String> handlePlateExit(@RequestBody WebhookEvent event) {
        String licensePlate = event.getLicensePlate();
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
                spot.setLicensePlate(null);
                spotRepository.save(spot);
            });

            return ResponseEntity.ok("Vehicle exited. Total price: R$ " + price);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not found");
        }
    }

    private double calculatePrice(long minutes) {
        double hourlyRate = 10.0;
        double hours = Math.max(1, Math.ceil(minutes / 60.0));
        long totalSpots = spotRepository.count();
        long freeSpots = spotRepository.findAll().stream().filter(spot -> !spot.getOccupied()).count();
        double occupancyRate = totalSpots > 0 ? 1.0 - (freeSpots / (double) totalSpots) : 0.0;
        double dynamicFactor = occupancyRate >= 0.8 ? 1.2 : 1.0;
        return hours * hourlyRate * dynamicFactor;
    }
}
