package org.example.garagemanagementapi.model;

import jakarta.persistence.*;

@Entity
public class Spot {

    @Id
    private Long id;  // Recebe o ID enviado pelo simulador, por isso não geramos

    private String sector;
    private double lat;
    private double lng;
    private boolean occupied;
    private String licensePlate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLng() { return lng; }
    public void setLng(double lng) { this.lng = lng; }

    public boolean isOccupied() { return occupied; }
    public void setOccupied(boolean occupied) { this.occupied = occupied; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
}
