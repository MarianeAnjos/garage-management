package org.example.garagemanagementapi.service;

import org.example.garagemanagementapi.model.Spot;
import org.example.garagemanagementapi.model.Vehicle;
import org.example.garagemanagementapi.repository.SpotRepository;
import org.example.garagemanagementapi.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
@Service
public class GarageService {

    private final VehicleRepository vehicleRepository;
    private final SpotRepository spotRepository;

    public GarageService(VehicleRepository vehicleRepository, SpotRepository spotRepository) {
        this.vehicleRepository = vehicleRepository;
        this.spotRepository = spotRepository;
    }

    public Vehicle registerEntry(String licensePlate, Double lat, Double lng) {
        Vehicle vehicle = new Vehicle(licensePlate, LocalDateTime.now());
        return vehicleRepository.save(vehicle);
    }

    public String parkVehicle(String licensePlate, Double lat, Double lng) {
        // Agora busca qualquer veículo, independente do exitTime
        Optional<Vehicle> vehicleOpt = vehicleRepository.findFirstByLicensePlate(licensePlate);

        if (vehicleOpt.isEmpty()) {
            throw new RuntimeException("Veículo não encontrado para estacionar");
        }

        Spot spot = spotRepository.findByLatAndLng(lat, lng)
                .orElseGet(() -> {
                    Spot newSpot = new Spot();
                    newSpot.setLat(lat);
                    newSpot.setLng(lng);
                    newSpot.setSector("A"); // ou lógica dinâmica
                    return spotRepository.save(newSpot);
                });

        spot.setOccupied(true);
        spotRepository.save(spot);

        Vehicle vehicle = vehicleOpt.get();
        vehicle.setSpot(spot);
        vehicleRepository.save(vehicle);

        return "Veículo estacionado";
    }

    public String registerExit(String licensePlate) {
        // Agora busca qualquer veículo, independente do exitTime
        Optional<Vehicle> vehicleOpt = vehicleRepository.findFirstByLicensePlate(licensePlate);

        if (vehicleOpt.isEmpty()) {
            throw new RuntimeException("Veículo não encontrado para saída");
        }

        Vehicle vehicle = vehicleOpt.get();
        vehicle.setExitTime(LocalDateTime.now());

        Spot spot = vehicle.getSpot();
        if (spot != null) {
            spot.setOccupied(false);
            spotRepository.save(spot);
        }

        vehicleRepository.save(vehicle);

        return "Saída registrada";
    }
}

