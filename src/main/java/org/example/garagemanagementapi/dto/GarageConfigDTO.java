package org.example.garagemanagementapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class GarageConfigDTO {
    private List<SectorDTO> garage;
    private List<SpotDTO> spots;
}
