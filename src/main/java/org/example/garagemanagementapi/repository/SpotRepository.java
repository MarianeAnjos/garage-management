package org.example.garagemanagementapi.repository;

import org.example.garagemanagementapi.model.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Long> {

    Optional<Spot> findByLatAndLng(Double lat, Double lng);
    Optional<Spot> findByLicensePlate(String plate);
    long countByOccupiedFalse();

    List<Spot> findAllBySector(String sector);
}
