package org.example.garagemanagementapi;

import org.example.garagemanagementapi.model.Spot;
import org.example.garagemanagementapi.repository.SpotRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GarageManagementApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(GarageManagementApiApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(SpotRepository spotRepository) {
        return args -> {
            if (spotRepository.findAll().isEmpty()) {
                Spot initialSpot = new Spot();

                initialSpot.setLat(-23.561684);
                initialSpot.setLng(-46.655981);
                initialSpot.setLicensePlate("LMN01");
                initialSpot.setOccupied(false);

                spotRepository.save(initialSpot);
                System.out.println("Spot salvo com sucesso!");
            } else {
                System.out.println("Spot já existe no banco");
            }
        };
    }

}
