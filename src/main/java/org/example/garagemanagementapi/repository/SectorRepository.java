package org.example.garagemanagementapi.repository;

import org.example.garagemanagementapi.model.Sector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SectorRepository extends JpaRepository<Sector, Long> {
    Optional<Sector> findBySector(String sector);
}
