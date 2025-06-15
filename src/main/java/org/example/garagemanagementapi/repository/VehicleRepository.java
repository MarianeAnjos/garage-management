package org.example.garagemanagementapi.repository;

import org.example.garagemanagementapi.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

     Optional<Vehicle> findFirstByLicensePlateAndExitTimeIsNull(String licensePlate);

     List<Vehicle> findByExitTimeIsNotNull();

     List<Vehicle> findByExitTimeBetween(LocalDateTime start, LocalDateTime end);

}
