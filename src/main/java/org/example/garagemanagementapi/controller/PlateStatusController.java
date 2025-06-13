package org.example.garagemanagementapi.controller;

import org.example.garagemanagementapi.dto.PlateStatusRequest;
import org.example.garagemanagementapi.dto.PlateStatusResponse;
import org.example.garagemanagementapi.model.Spot;
import org.example.garagemanagementapi.model.Vehicle;
import org.example.garagemanagementapi.repository.SpotRepository;
import org.example.garagemanagementapi.repository.VehicleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/plate-status")
public class PlateStatusController {

    private final VehicleRepository vehicleRepository;
    private final SpotRepository spotRepository;

    public PlateStatusController(VehicleRepository vehicleRepository, SpotRepository spotRepository) {
        this.vehicleRepository = vehicleRepository;
        this.spotRepository = spotRepository;
    }

    @PostMapping
    public ResponseEntity<?> getPlateStatus(@RequestBody PlateStatusRequest request) {
        String licensePlate = request.getLicensePlate();

        Optional<Vehicle> vehicleOpt = vehicleRepository.findFirstByLicensePlateAndExitTimeIsNull(licensePlate);
        if (vehicleOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Veículo não encontrado ou já finalizado.");
        }

        Vehicle vehicle = vehicleOpt.get();
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(vehicle.getEntryTime(), now);
        long minutes = duration.toMinutes();
        double price = calculatePrice(minutes);

        Optional<Spot> spotOpt = spotRepository.findByLicensePlate(licensePlate);

        PlateStatusResponse response = new PlateStatusResponse();
        response.setLicensePlate(vehicle.getLicensePlate());
        response.setEntryTime(vehicle.getEntryTime());
        response.setTimeParked(now);
        response.setPriceUntilNow(price);

        if (spotOpt.isPresent()) {
            Spot spot = spotOpt.get();
            response.setLat(spot.getLat());
            response.setLng(spot.getLng());
        }

        return ResponseEntity.ok(response);
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
