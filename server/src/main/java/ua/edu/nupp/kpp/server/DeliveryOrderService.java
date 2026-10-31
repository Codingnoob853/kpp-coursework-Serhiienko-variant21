package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;

public class DeliveryOrderService {
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
        requireShipment(order.getShipmentId());
        dao.add(order);
    }

    public void update(DeliveryOrder order) throws SQLException {
        if (order == null || order.getId() <= 0) {
            throw new IllegalArgumentException("Некоректний ID замовлення для оновлення");
        }
        validate(order);
        requireClient(order.getClientId());
        requireCourier(order.getCourierId());
        requireShipment(order.getShipmentId());
        dao.update(order);
    }

    public void updateStatus(long id, String status) throws SQLException {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Статус замовлення не може бути порожнім");
        }
        dao.updateStatus(id, status);
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
    }

    private void validate(DeliveryOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Замовлення не може бути null");
        }
        if (order.getOrderDate() == null || order.getOrderDate().isBlank()) {
            throw new IllegalArgumentException("Дата замовлення є обов'язковою");
        }
        if (order.getTotalCost() < 0) {
            throw new IllegalArgumentException("Загальна вартість замовлення не може бути від'ємною");
        }
    }

    private void requireClient(long clientId) throws SQLException {
        if (clientDAO.getById(clientId) == null) {
            throw new IllegalArgumentException("Клієнта з ID " + clientId + " не знайдено");
        }
    }

    private void requireCourier(long courierId) throws SQLException {
        if (courierDAO.getById(courierId) == null) {
            throw new IllegalArgumentException("Кур'єра з ID " + courierId + " не знайдено");
        }
    }

    private void requireShipment(long shipmentId) throws SQLException {
        if (shipmentDAO.getById(shipmentId) == null) {
            throw new IllegalArgumentException("Відправлення з ID " + shipmentId + " не знайдено");
        }
    }
}