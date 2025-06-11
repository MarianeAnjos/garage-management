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
            Spot spot1 = new Spot();
            spot1.setLat(-23.561684);
            spot1.setLongi(-46.655981);
            spot1.setLicensePlate("LMN01");
            spot1.setOccupied(false);

            spotRepository.save(spot1);
            System.out.println("Spot salvo com sucesso!");
        };
    }
}
