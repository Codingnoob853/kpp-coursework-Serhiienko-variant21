package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class DeliveryOrderService {
    private static final Logger LOG = Logger.getLogger(DeliveryOrderService.class.getName());

    private final DeliveryOrderDAO dao = new DeliveryOrderDAO();
    private final ClientDAO clientDAO = new ClientDAO();
    private final CourierDAO courierDAO = new CourierDAO();
    private final ShipmentDAO shipmentDAO = new ShipmentDAO();

    public List<DeliveryOrder> getAll() throws SQLException {
        return dao.getAll();
    }

    public DeliveryOrder getById(long id) throws SQLException {
        return dao.getById(id);
    }

    public void add(DeliveryOrder order) throws SQLException {
        validate(order);
        requireClient(order.getClientId());
        requireCourier(order.getCourierId());

        Shipment shipment = requireShipment(order.getShipmentId());

        double f1 = RiskCalculator.factor1(shipment.getWeight(), 2.0);
        double f2 = order.getF2() > 0 ? order.getF2() : 50.0;
        double f3 = order.getF3() > 0 ? order.getF3() : 60.0;

        double riskIndex = RiskCalculator.calculate(f1, f2, f3);

        order.setF1(f1);
        order.setF2(f2);
        order.setF3(f3);
        order.setRiskIndex(riskIndex);
        order.setStatus(OrderStatus.CREATED.name());

        dao.add(order);
        LOG.info("Delivery order added");
    }

    public void update(DeliveryOrder order) throws SQLException {
        if (order == null || order.getId() <= 0) {
            throw new IllegalArgumentException("Invalid order ID for update");
        }
        validate(order);
        requireClient(order.getClientId());
        requireCourier(order.getCourierId());

        Shipment shipment = requireShipment(order.getShipmentId());

        double f1 = RiskCalculator.factor1(shipment.getWeight(), 2.0);
        double f2 = order.getF2();
        double f3 = order.getF3();
        double riskIndex = RiskCalculator.calculate(f1, f2, f3);

        order.setF1(f1);
        order.setF2(f2);
        order.setF3(f3);
        order.setRiskIndex(riskIndex);

        dao.update(order);
        LOG.info("Delivery order updated");
    }

    public void updateStatus(long id, String status) throws SQLException {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Order status must not be blank");
        }
        dao.updateStatus(id, status);
        LOG.info("Updated order ID " + id + " status to " + status);
    }

    public static boolean isAllowedTransition(OrderStatus from, OrderStatus to) {
        return (from == OrderStatus.CREATED && to == OrderStatus.ASSIGNED)
                || (from == OrderStatus.ASSIGNED && to == OrderStatus.DELIVERED)
                || (from == OrderStatus.DELIVERED && to == OrderStatus.CANCELLED);
    }

    public void changeState(long id, OrderStatus next) throws SQLException {
        DeliveryOrder current = dao.getById(id);
        if (current == null) {
            throw new IllegalArgumentException("Order with ID " + id + " not found");
        }

        OrderStatus currentStatus = OrderStatus.valueOf(current.getStatus());

        if (!isAllowedTransition(currentStatus, next)) {
            LOG.warning("Invalid state transition: " + currentStatus + " -> " + next);
            throw new IllegalArgumentException("Invalid state transition");
        }

        dao.updateStatus(id, next.name());
        LOG.info("Changed order state: " + currentStatus + " -> " + next);
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
        LOG.info("Deleted delivery order with ID " + id);
    }

    private void validate(DeliveryOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Order must not be null");
        }
        if (order.getOrderDate() == null || order.getOrderDate().isBlank()) {
            throw new IllegalArgumentException("Order date is required");
        }
        if (order.getTotalCost() < 0) {
            throw new IllegalArgumentException("Total order cost must not be negative");
        }
    }

    private void requireClient(long clientId) throws SQLException {
        if (clientDAO.getById(clientId) == null) {
            throw new IllegalArgumentException("Client with ID " + clientId + " not found");
        }
    }

    private void requireCourier(long courierId) throws SQLException {
        if (courierDAO.getById(courierId) == null) {
            throw new IllegalArgumentException("Courier with ID " + courierId + " not found");
        }
    }

    private Shipment requireShipment(long shipmentId) throws SQLException {
        Shipment shipment = shipmentDAO.getById(shipmentId);
        if (shipment == null) {
            throw new IllegalArgumentException("Shipment with ID " + shipmentId + " not found");
        }
        return shipment;
    }
}