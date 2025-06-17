package org.example.garagemanagementapi.controller;

import org.example.garagemanagementapi.dto.RevenueResponse;
import org.example.garagemanagementapi.model.Vehicle;
import org.example.garagemanagementapi.repository.VehicleRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/revenue")
public class RevenueController {

    private final VehicleRepository vehicleRepository;

    public RevenueController(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @GetMapping
    public ResponseEntity<RevenueResponse> getRevenue(
            @RequestParam(name = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        List<Vehicle> vehicles;

        if (date != null) {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
            vehicles = vehicleRepository.findByExitTimeBetween(startOfDay, endOfDay);
        } else {
            vehicles = vehicleRepository.findByExitTimeIsNotNull();
        }

        BigDecimal totalRevenue = vehicles.stream()
                .map(v -> v.getPrice() != null && v.getPrice().compareTo(BigDecimal.ZERO) >= 0
                        ? v.getPrice()
                        : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        RevenueResponse response = new RevenueResponse();
        response.setAmount(totalRevenue.doubleValue()); // ou .setAmount(totalRevenue) se usar BigDecimal no DTO
        response.setCurrency("BRL");
        response.setTimestamp(LocalDateTime.now());

        return ResponseEntity.ok(response);
    }
}

