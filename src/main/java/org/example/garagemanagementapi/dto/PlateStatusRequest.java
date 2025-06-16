package org.example.garagemanagementapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Requisição com a placa para consultar status do veículo.")
public class PlateStatusRequest {

    @Schema(description = "Placa do veículo a ser consultada", example = "XYZ9876", required = true)
    @JsonProperty("licensePlate")
    private String licensePlate;

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}
