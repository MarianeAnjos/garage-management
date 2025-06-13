package org.example.garagemanagementapi.dto;

import java.time.LocalDateTime;

public class PlateStatusResponse {
    private String licensePlate;
    private LocalDateTime entryTime;
    private LocalDateTime timeParked;
    private Double priceUntilNow;
    private Double lat;
    private Double lng;

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime getTimeParked() {
        return timeParked;
    }

    public void setTimeParked(LocalDateTime timeParked) {
        this.timeParked = timeParked;
    }

    public Double getPriceUntilNow() {
        return priceUntilNow;
    }

    public void setPriceUntilNow(Double priceUntilNow) {
        this.priceUntilNow = priceUntilNow;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
