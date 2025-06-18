package org.example.garagemanagementapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GarageController {

    @GetMapping("/garage")
    public String garage() {
        return "OK - Garage endpoint working";
    }
}
