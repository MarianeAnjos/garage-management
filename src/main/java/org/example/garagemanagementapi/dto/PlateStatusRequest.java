package org.example.garagemanagementapi.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Requisição com a placa para consultar status do veículo.")
public class PlateStatusRequest {

    @NotBlank(message = "Placa do veículo é obrigatória")
    @Schema(description = "Placa do veículo a ser consultada", example = "XYZ9876", required = true)
    private String licensePlate;

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}
