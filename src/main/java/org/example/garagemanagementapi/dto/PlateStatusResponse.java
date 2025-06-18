package org.example.garagemanagementapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Resposta com status da placa, tempo e valor atual")
public class PlateStatusResponse {

    @Schema(description = "Placa do veículo", example = "XYZ9876")
    private String licensePlate;

    @Schema(description = "Horário de entrada do veículo")
    private LocalDateTime entryTime;

    @Schema(description = "Horário atual ou de consulta")
    private LocalDateTime timeParked;

    @Schema(description = "Valor parcial até o momento", example = "7.50")
    private Double priceUntilNow;

    @Schema(description = "Latitude da vaga", example = "-23.561111")
    private Double lat;

    @Schema(description = "Longitude da vaga", example = "-46.653111")
    private Double lng;

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public LocalDateTime getEntryTime() { return entryTime; }
    public void setEntryTime(LocalDateTime entryTime) { this.entryTime = entryTime; }

    public LocalDateTime getTimeParked() { return timeParked; }
    public void setTimeParked(LocalDateTime timeParked) { this.timeParked = timeParked; }

    public Double getPriceUntilNow() { return priceUntilNow; }
    public void setPriceUntilNow(Double priceUntilNow) { this.priceUntilNow = priceUntilNow; }

    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }

    public Double getLng() { return lng; }
    public void setLng(Double lng) { this.lng = lng; }
}
