package org.example.garagemanagementapi.dto;

import lombok.Data;

@Data
public class SectorDTO {
    private String sector;
    private double basePrice;
    private int max_capacity;
    private String open_hour;
    private String close_hour;
    private int duration_limit_minutes;
}
