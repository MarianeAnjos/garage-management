package org.example.garagemanagementapi.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column
    private Double basePrice;

    @Column
    private Integer maxCapacity;

    @Column
    private String openHour;

    @Column
    private String closeHour;

    @Column
    private Integer durationLimitMinutes;
}
