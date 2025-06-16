package org.example.garagemanagementapi.service;

import org.example.garagemanagementapi.model.Spot;
import org.example.garagemanagementapi.model.Vehicle;
import org.example.garagemanagementapi.repository.SpotRepository;
import org.example.garagemanagementapi.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class GarageService {

    private final VehicleRepository vehicleRepo;
    private final SpotRepository spotRepo;
    private final BillingService billingService;

    public GarageService(VehicleRepository vRepo, SpotRepository sRepo, BillingService billingService) {
        this.vehicleRepo = vRepo;
        this.spotRepo = sRepo;
        this.billingService = billingService;
    }

    private Spot validarERegistrarVaga(Double lat, Double lng, String licensePlate) {
        Spot spot = spotRepo.findByLatAndLng(lat, lng)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada nas coordenadas."));

        if (spot.isOccupied()) {
            throw new RuntimeException("Vaga já está ocupada.");
        }

        spot.setLicensePlate(licensePlate);
        spot.setOccupied(true);
        spotRepo.save(spot);

        return spot;
    }

    private void registrarVeiculo(String licensePlate, LocalDateTime entrada) {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(licensePlate);
        vehicle.setEntryTime(entrada);
        vehicleRepo.save(vehicle);
    }

    public String registerEntry(String licensePlate, Double lat, Double lng) {
        if (lat == null || lng == null) {
            throw new RuntimeException("Latitude e longitude são obrigatórias.");
        }

        Spot spot = validarERegistrarVaga(lat, lng, licensePlate);
        registrarVeiculo(licensePlate, LocalDateTime.now());

        return "Veículo com placa " + licensePlate + " entrou pela cancela e foi vinculado à vaga " + spot.getId();
    }

    public String parkVehicle(String licensePlate, Double lat, Double lng) {
        if (lat == null || lng == null) {
            throw new RuntimeException("Latitude e longitude são obrigatórias.");
        }

        Spot spot = validarERegistrarVaga(lat, lng, licensePlate);
        registrarVeiculo(licensePlate, LocalDateTime.now());

        return "Veículo com placa " + licensePlate + " estacionado na vaga " + spot.getId();
    }

    public String registerExit(String licensePlate) {
        Vehicle vehicle = vehicleRepo.findFirstByLicensePlateAndExitTimeIsNull(licensePlate)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado ou já saiu."));

        LocalDateTime exitTime = LocalDateTime.now();
        long minutes = Duration.between(vehicle.getEntryTime(), exitTime).toMinutes();

        int totalSpots = (int) spotRepo.count();
        int freeSpots = (int) spotRepo.countByOccupiedFalse();
        double price = billingService.calculateCharge(minutes, totalSpots, freeSpots, 10.0);

        vehicle.setExitTime(exitTime);
        vehicle.setPrice(price);
        vehicleRepo.save(vehicle);

        spotRepo.findByLicensePlate(licensePlate).ifPresent(spot -> {
            spot.setOccupied(false);
            spot.setLicensePlate(null);
            spotRepo.save(spot);
        });

        return "Veículo com placa " + licensePlate + " saiu. Valor total: R$ " + String.format("%.2f", price);
    }
}
