package org.example.garagemanagementapi.service;

import org.springframework.stereotype.Service;

@Service
public class PricingService {

    public double calculatePrice(long minutes, int totalSpots, int freeSpots, double basePrice) {
        if (totalSpots == 0) return basePrice;

        double occupancyRatio = 1.0 - ((double) freeSpots / totalSpots);
        double hours = Math.max(1.0, Math.ceil(minutes / 60.0));
        double price = basePrice * hours;

        if (occupancyRatio < 0.25) {
            return price * 0.90; // 10% de desconto
        } else if (occupancyRatio < 0.50) {
            return price; // preço base
        } else if (occupancyRatio < 0.75) {
            return price * 1.10; // acréscimo de 10%
        } else {
            return price * 1.25; // acréscimo de 25%
        }
    }
}
