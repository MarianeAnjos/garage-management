/*package org.example.garagemanagementapi.controller;

import org.example.garagemanagementapi.service.BillingService;
import org.example.garagemanagementapi.service.PricingService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BillingServiceTest {

    // Se BillingService injeta PricingService, você pode instanciar ambos manualmente
    private PricingService pricingService = new PricingService();
    private BillingService billingService = new BillingService(pricingService); // supondo um construtor que recebe PricingService

    @Test
    public void testCalculateCharge() {
        long minutesParked = 60;
        int totalSpots = 100;
        int freeSpots = 80;
        double basePrice = 10.0;
        double expectedCharge = basePrice * 0.9;

        double actualCharge = billingService.calculateCharge(minutesParked, totalSpots, freeSpots, basePrice);
        assertEquals(expectedCharge, actualCharge, 0.0001);
    }
}
*/