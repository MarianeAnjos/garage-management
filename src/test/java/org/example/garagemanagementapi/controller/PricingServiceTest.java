/*package org.example.garagemanagementapi.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PricingServiceTest {

    private PricingService pricingService = new PricingService();

    @Test
    public void testCalculatePriceWithLowOccupancy() {
        // Exemplo: totalSpots = 100, freeSpots = 80 (80% livre, ou 20% ocupado), 10% de desconto
        long minutesParked = 60; // Não utilizado nesta lógica simples
        int totalSpots = 100;
        int freeSpots = 80;
        double basePrice = 10.0;
        double expectedPrice = basePrice * 0.9; // 10% de desconto

        double calculatedPrice = pricingService.calculatePrice(minutesParked, totalSpots, freeSpots, basePrice);
        assertEquals(expectedPrice, calculatedPrice, 0.01);
    }
}
*/