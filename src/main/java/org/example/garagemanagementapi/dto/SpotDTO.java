package org.example.garagemanagementapi.dto;

import lombok.Data;

@Data
public class SpotDTO {
    private Long id;
    private String sector;
    private double lat;
    private double lng;
}
