package org.example.garagemanagementapi.repository;

import org.example.garagemanagementapi.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

     Optional<Vehicle> findFirstByLicensePlateAndExitTimeIsNull(String plate);
     List<Vehicle> findByExitTimeBetween(LocalDateTime start, LocalDateTime end);
     List<Vehicle> findByExitTimeIsNotNull();
}
