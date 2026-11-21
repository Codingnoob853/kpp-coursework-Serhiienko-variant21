package ua.edu.nupp.kpp.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourierServerUnitTest {

    @Test
    void factor1BelowLimitReturnsZero() {
        assertEquals(0.0, RiskCalculator.factor1(1.5, 2.0), 1e-9);
    }

    @Test
    void factor1AboveLimitCalculatesPercentage() {
        assertEquals(50.0, RiskCalculator.factor1(3.0, 2.0), 1e-9);
    }

    @Test
    void factor1ClampsAtHundred() {
        assertEquals(100.0, RiskCalculator.factor1(10.0, 2.0), 1e-9);
    }

    @Test
    void factor1ThrowsOnNegativeWeightOrNonPositiveLimit() {
        assertThrows(IllegalArgumentException.class, () -> RiskCalculator.factor1(-0.1, 2.0));
        assertThrows(IllegalArgumentException.class, () -> RiskCalculator.factor1(1.0, 0.0));
    }

    @Test
    void calculateAppliesWeightsAndRoundsToTwoDecimals() {
        // 0.4 * 50 + 0.35 * 40 + 0.25 * 20 = 20 + 14 + 5 = 39.0
        assertEquals(39.0, RiskCalculator.calculate(50.0, 40.0, 20.0), 1e-9);
    }

    @Test
    void calculateThrowsWhenFactorOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> RiskCalculator.calculate(-1.0, 50.0, 50.0));
        assertThrows(IllegalArgumentException.class, () -> RiskCalculator.calculate(50.0, 100.1, 50.0));
        assertThrows(IllegalArgumentException.class, () -> RiskCalculator.calculate(50.0, 50.0, 120.0));
    }

    @Test
    void allowedStatusTransitionsReturnTrue() {
        assertTrue(DeliveryOrderService.isAllowedTransition(OrderStatus.CREATED, OrderStatus.ASSIGNED));
        assertTrue(DeliveryOrderService.isAllowedTransition(OrderStatus.ASSIGNED, OrderStatus.DELIVERED));
        assertTrue(DeliveryOrderService.isAllowedTransition(OrderStatus.DELIVERED, OrderStatus.CANCELLED));
    }

    @Test
    void disallowedStatusTransitionsReturnFalse() {
        assertFalse(DeliveryOrderService.isAllowedTransition(OrderStatus.CREATED, OrderStatus.DELIVERED));
        assertFalse(DeliveryOrderService.isAllowedTransition(OrderStatus.ASSIGNED, OrderStatus.CREATED));
        assertFalse(DeliveryOrderService.isAllowedTransition(OrderStatus.CANCELLED, OrderStatus.ASSIGNED));
    }

    @Test
    void deliveryOrderGettersAndSettersStoreValues() {
        DeliveryOrder order = new DeliveryOrder();
        order.setId(21L);
        order.setClientId(5L);
        order.setCourierId(3L);
        order.setShipmentId(9L);
        order.setOrderDate("2026-11-21");
        order.setStatus("CREATED");
        order.setTotalCost(145.5);
        order.setRiskIndex(39.0);

        assertEquals(21L, order.getId());
        assertEquals(5L, order.getClientId());
        assertEquals(3L, order.getCourierId());
        assertEquals(9L, order.getShipmentId());
        assertEquals("2026-11-21", order.getOrderDate());
        assertEquals("CREATED", order.getStatus());
        assertEquals(145.5, order.getTotalCost(), 1e-9);
        assertEquals(39.0, order.getRiskIndex(), 1e-9);
    }

    @Test
    void deliveryOrderParameterizedConstructorSetsFields() {
        DeliveryOrder order = new DeliveryOrder(
                10L, 2L, 4L, 6L, "2026-11-20", "ASSIGNED", 250.0, 25.0, 50.0, 60.0, 42.5
        );
        assertEquals(10L, order.getId());
        assertEquals("ASSIGNED", order.getStatus());
        assertEquals(250.0, order.getTotalCost(), 1e-9);
        assertEquals(42.5, order.getRiskIndex(), 1e-9);
    }

    @Test
    void zeroRiskFactorsProduceZeroIndex() {
        assertEquals(0.0, RiskCalculator.calculate(0.0, 0.0, 0.0), 1e-9);
    }

    @Test
    void maxRiskFactorsProduceHundredIndex() {
        assertEquals(100.0, RiskCalculator.calculate(100.0, 100.0, 100.0), 1e-9);
    }
}