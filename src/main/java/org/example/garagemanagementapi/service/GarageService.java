package org.example.garagemanagementapi.service;

import org.example.garagemanagementapi.model.Sector;
import org.example.garagemanagementapi.model.Spot;
import org.example.garagemanagementapi.model.Vehicle;
import org.example.garagemanagementapi.repository.SectorRepository;
import org.example.garagemanagementapi.repository.SpotRepository;
import org.example.garagemanagementapi.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GarageService {

    private final VehicleRepository vehicleRepository;
    private final SpotRepository spotRepository;
    private final SectorRepository sectorRepository;

    public GarageService(VehicleRepository vehicleRepository, SpotRepository spotRepository, SectorRepository sectorRepository) {
        this.vehicleRepository = vehicleRepository;
        this.spotRepository = spotRepository;
        this.sectorRepository = sectorRepository;
    }

    public Vehicle registerEntry(String licensePlate, Double lat, Double lng) {
        Spot spot = spotRepository.findByLatAndLng(lat, lng)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada para entrada"));

        String sectorName = spot.getSector();
        Sector sector = sectorRepository.findBySector(sectorName)
                .orElseThrow(() -> new RuntimeException("Setor não encontrado"));

        double occupationRate = getSectorOccupationRate(sectorName);
        double dynamicPrice = calculateDynamicPrice(sector.getBasePrice(), occupationRate);

        Vehicle vehicle = new Vehicle(licensePlate, LocalDateTime.now());
        vehicle.setPrice(BigDecimal.valueOf(dynamicPrice));
        vehicleRepository.save(vehicle);

        return vehicle;
    }

    public String parkVehicle(String licensePlate, Double lat, Double lng) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findFirstByLicensePlate(licensePlate);

        if (vehicleOpt.isEmpty()) {
            throw new RuntimeException("Veículo não encontrado para estacionar");
        }

        Spot spot = spotRepository.findByLatAndLng(lat, lng)
                .orElseThrow(() -> new RuntimeException("Vaga não registrada na garagem"));

        spot.setOccupied(true);
        spot.setLicensePlate(licensePlate);
        spotRepository.save(spot);

        Vehicle vehicle = vehicleOpt.get();
        vehicle.setSpot(spot);
        vehicleRepository.save(vehicle);

        return "Veículo estacionado";
    }

    public String registerExit(String licensePlate) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findFirstByLicensePlate(licensePlate);

        if (vehicleOpt.isEmpty()) {
            throw new RuntimeException("Veículo não encontrado para saída");
        }

        Vehicle vehicle = vehicleOpt.get();
        vehicle.setExitTime(LocalDateTime.now());

        Spot spot = vehicle.getSpot();
        if (spot != null) {
            spot.setOccupied(false);
            spot.setLicensePlate(null);
            spotRepository.save(spot);
        }

        vehicleRepository.save(vehicle);

        return "Saída registrada";
    }

    private double getSectorOccupationRate(String sectorName) {
        List<Spot> sectorSpots = spotRepository.findAllBySector(sectorName);
        long total = sectorSpots.size();
        long occupied = sectorSpots.stream().filter(Spot::isOccupied).count();

        return total == 0 ? 0 : (double) occupied / total;
    }

    private double calculateDynamicPrice(double basePrice, double occupationRate) {
        if (occupationRate < 0.25) {
            return basePrice * 0.9;
        } else if (occupationRate < 0.5) {
            return basePrice;
        } else if (occupationRate < 0.75) {
            return basePrice * 1.10;
        } else {
            return basePrice * 1.25;
        }
    }
}
