package ua.edu.nupp.kpp.server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourierServerUnitTest {

    @Test
    @DisplayName("Тест №1: відсутність перевищення нормативи для F1")
    void factor1BelowLimitReturnsZero() {
        // Arrange
        double weight = 1.5;
        double limitWeight = 2.0;
        double expected = 0.0;

        // Act
        double actual = RiskCalculator.factor1(weight, limitWeight);

        // Assert
        assertEquals(expected, actual, 1e-9);
    }

    @Test
    @DisplayName("Тест №2: позитивний розрахунок F1 при перевищенні нормативи")
    void factor1AboveLimitCalculatesPercentage() {
        // Arrange
        double weight = 3.0;
        double limitWeight = 2.0;
        double expected = 50.0;

        // Act
        double actual = RiskCalculator.factor1(weight, limitWeight);

        // Assert
        assertEquals(expected, actual, 1e-9);
    }

    @Test
    @DisplayName("Тест №3: обмеження F1 верхньою межею 100")
    void factor1ClampsAtHundred() {
        // Arrange
        double weight = 10.0;
        double limitWeight = 2.0;
        double expected = 100.0;

        // Act
        double actual = RiskCalculator.factor1(weight, limitWeight);

        // Assert
        assertEquals(expected, actual, 1e-9);
    }

    @Test
    @DisplayName("Тест №4: виняток при від'ємній вазі у factor1")
    void factor1ThrowsOnNegativeWeight() {
        // Arrange
        double weight = -0.1;
        double limitWeight = 2.0;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> RiskCalculator.factor1(weight, limitWeight));
    }

    @Test
    @DisplayName("Тест №5: виняток при нульовому або від'ємному ліміті у factor1")
    void factor1ThrowsOnNonPositiveLimit() {
        // Arrange
        double weight = 1.0;
        double limitWeight = 0.0;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> RiskCalculator.factor1(weight, limitWeight));
    }

    @Test
    @DisplayName("Тест №6: розрахунок інтегрального показника з округленням")
    void calculateAppliesWeightsAndRoundsToTwoDecimals() {
        // Arrange
        double f1 = 50.0;
        double f2 = 40.0;
        double f3 = 20.0;
        double expected = 39.0; // 0.40 * 50 + 0.35 * 40 + 0.25 * 20

        // Act
        double actual = RiskCalculator.calculate(f1, f2, f3);

        // Assert
        assertEquals(expected, actual, 1e-9);
    }

    @Test
    @DisplayName("Тест №7: виняток при значенні фактора вище 100")
    void calculateThrowsWhenFactorAboveHundred() {
        // Arrange
        double f1 = 101.0;
        double f2 = 10.0;
        double f3 = 10.0;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> RiskCalculator.calculate(f1, f2, f3));
    }

    @Test
    @DisplayName("Тест №8: виняток при від'ємному значенні фактора")
    void calculateThrowsWhenFactorIsNegative() {
        // Arrange
        double f1 = -1.0;
        double f2 = 50.0;
        double f3 = 50.0;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> RiskCalculator.calculate(f1, f2, f3));
    }

    @Test
    @DisplayName("Тест №9: дозволений перехід CREATED -> ASSIGNED")
    void createdToAssignedTransitionAllowed() {
        // Arrange
        OrderStatus from = OrderStatus.CREATED;
        OrderStatus to = OrderStatus.ASSIGNED;

        // Act
        boolean allowed = DeliveryOrderService.isAllowedTransition(from, to);

        // Assert
        assertTrue(allowed);
    }

    @Test
    @DisplayName("Тест №10: дозволений перехід ASSIGNED -> DELIVERED")
    void assignedToDeliveredTransitionAllowed() {
        // Arrange
        OrderStatus from = OrderStatus.ASSIGNED;
        OrderStatus to = OrderStatus.DELIVERED;

        // Act
        boolean allowed = DeliveryOrderService.isAllowedTransition(from, to);

        // Assert
        assertTrue(allowed);
    }

    @Test
    @DisplayName("Тест №11: дозволений перехід DELIVERED -> CANCELLED")
    void deliveredToCancelledTransitionAllowed() {
        // Arrange
        OrderStatus from = OrderStatus.DELIVERED;
        OrderStatus to = OrderStatus.CANCELLED;

        // Act
        boolean allowed = DeliveryOrderService.isAllowedTransition(from, to);

        // Assert
        assertTrue(allowed);
    }

    @Test
    @DisplayName("Тест №12: заборонений перехід CREATED -> DELIVERED")
    void createdToDeliveredTransitionDisallowed() {
        // Arrange
        OrderStatus from = OrderStatus.CREATED;
        OrderStatus to = OrderStatus.DELIVERED;

        // Act
        boolean allowed = DeliveryOrderService.isAllowedTransition(from, to);

        // Assert
        assertFalse(allowed);
    }

    @Test
    @DisplayName("Тест №13: заборонений перехід CREATED -> CANCELLED через проміжні стани")
    void createdToCancelledTransitionDisallowed() {
        // Arrange
        OrderStatus from = OrderStatus.CREATED;
        OrderStatus to = OrderStatus.CANCELLED;

        // Act
        boolean allowed = DeliveryOrderService.isAllowedTransition(from, to);

        // Assert
        assertFalse(allowed);
    }

    @Test
    @DisplayName("Тест №14: заборонений зворотний перехід ASSIGNED -> CREATED")
    void assignedToCreatedTransitionDisallowed() {
        // Arrange
        OrderStatus from = OrderStatus.ASSIGNED;
        OrderStatus to = OrderStatus.CREATED;

        // Act
        boolean allowed = DeliveryOrderService.isAllowedTransition(from, to);

        // Assert
        assertFalse(allowed);
    }

    @Test
    @DisplayName("Тест №15: коректне збереження та читання полів DeliveryOrder")
    void deliveryOrderGettersAndSettersStoreValues() {
        // Arrange
        DeliveryOrder order = new DeliveryOrder();

        // Act
        order.setId(21L);
        order.setClientId(5L);
        order.setCourierId(3L);
        order.setShipmentId(9L);
        order.setOrderDate("2026-11-21");
        order.setStatus("CREATED");
        order.setTotalCost(145.5);
        order.setRiskIndex(39.0);

        // Assert
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
    @DisplayName("Тест №16: нульові фактори дають нульовий інтегральний показник")
    void zeroRiskFactorsProduceZeroIndex() {
        // Arrange
        double f1 = 0.0;
        double f2 = 0.0;
        double f3 = 0.0;
        double expected = 0.0;

        // Act
        double actual = RiskCalculator.calculate(f1, f2, f3);

        // Assert
        assertEquals(expected, actual, 1e-9);
    }
}