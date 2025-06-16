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

        };
    }


}
