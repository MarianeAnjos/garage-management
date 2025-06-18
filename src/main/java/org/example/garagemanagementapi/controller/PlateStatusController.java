package org.example.garagemanagementapi.controller;

import org.example.garagemanagementapi.dto.PlateStatusRequest;
import org.example.garagemanagementapi.dto.PlateStatusResponse;
import org.example.garagemanagementapi.model.Vehicle;
import org.example.garagemanagementapi.repository.SpotRepository;
import org.example.garagemanagementapi.repository.VehicleRepository;
import org.example.garagemanagementapi.service.BillingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
@RestController
@RequestMapping("/plate-status")
public class PlateStatusController {

    private final VehicleRepository vehicleRepo;
    private final SpotRepository spotRepo;
    private final BillingService billingService;

    public PlateStatusController(VehicleRepository vehicleRepo,
                                 SpotRepository spotRepo,
                                 BillingService billingService) {
        this.vehicleRepo = vehicleRepo;
        this.spotRepo = spotRepo;
        this.billingService = billingService;
    }

    @PostMapping
    public ResponseEntity<PlateStatusResponse> getPlateStatus(@Valid @RequestBody PlateStatusRequest request) {
        String licensePlate = request.getLicensePlate();

        Vehicle vehicle = vehicleRepo.findFirstByLicensePlate(licensePlate)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado."));

        LocalDateTime now = LocalDateTime.now();
        long minutes = Duration.between(vehicle.getEntryTime(), now).toMinutes();

        int totalSpots = (int) spotRepo.count();
        int freeSpots = (int) spotRepo.countByOccupiedFalse();
        double price = billingService.calculateCharge(minutes, totalSpots, freeSpots, 10.0);

        PlateStatusResponse response = new PlateStatusResponse();
        response.setLicensePlate(vehicle.getLicensePlate());
        response.setEntryTime(vehicle.getEntryTime());
        response.setTimeParked(now);
        response.setPriceUntilNow(price);

        spotRepo.findByLicensePlate(licensePlate).ifPresent(spot -> {
            response.setLat(spot.getLat());
            response.setLng(spot.getLng());
        });

        return ResponseEntity.ok(response);
    }
}


