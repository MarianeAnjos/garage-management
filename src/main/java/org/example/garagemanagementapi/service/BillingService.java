package org.example.garagemanagementapi.service;

import org.springframework.stereotype.Service;

@Service
public class BillingService {
    private final PricingService pricingService;

    public BillingService(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    public double calculateCharge(long minutesParked, int totalSpots, int freeSpots, double basePrice) {
        return pricingService.calculatePrice(minutesParked, totalSpots, freeSpots, basePrice);
    }
}
