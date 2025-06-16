/*package org.example.garagemanagementapi.controller.repository;

import org.example.garagemanagementapi.model.Spot;
import org.example.garagemanagementapi.repository.SpotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SpotRepositoryTest {

    @Autowired
    private SpotRepository spotRepository;

    @Test
    @Rollback(false)
    public void testFindByLatAndLng() {
        Spot spot = new Spot();
        spot.setLat(-23.561684);
        spot.setLng(-46.625378);
        spot.setOccupied(false);
        spot.setLicensePlate(null);
        spotRepository.save(spot);

        Optional<Spot> found = spotRepository.findByLatAndLng(spot.getLat(), spot.getLng());

        assertThat(found).isPresent();
        assertThat(found.get().isOccupied()).isFalse(); // ✅ corrigido aqui
    }
}
*/