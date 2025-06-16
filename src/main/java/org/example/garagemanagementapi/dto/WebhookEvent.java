package org.example.garagemanagementapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class WebhookEvent {

    @JsonProperty("event_type")
    private String event;

    @NotBlank(message = "Placa do veículo é obrigatória")
    private String licensePlate;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public @NotBlank(message = "Placa do veículo é obrigatória") String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(@NotBlank(message = "Placa do veículo é obrigatória") String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public @NotNull(message = "Latitude é obrigatória") Double getLat() {
        return lat;
    }

    public void setLat(@NotNull(message = "Latitude é obrigatória") Double lat) {
        this.lat = lat;
    }

    public @NotNull(message = "Longitude é obrigatória") Double getLng() {
        return lng;
    }

    public void setLng(@NotNull(message = "Longitude é obrigatória") Double lng) {
        this.lng = lng;
    }

    @NotNull(message = "Latitude é obrigatória")
    private Double lat;

    @NotNull(message = "Longitude é obrigatória")
    private Double lng;
}
