package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;

public class ShipmentService {
    private final ShipmentDAO dao = new ShipmentDAO();

    public List<Shipment> getAll() throws SQLException {
        return dao.getAll();
    }

    public Shipment getById(long id) throws SQLException {
        return dao.getById(id);
    }

    public void add(Shipment shipment) throws SQLException {
        validate(shipment);
        dao.add(shipment);
    }

    public void update(Shipment shipment) throws SQLException {
        if (shipment == null || shipment.getId() <= 0) {
            throw new IllegalArgumentException("Некоректний ID відправлення для оновлення");
        }
        validate(shipment);
        dao.update(shipment);
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
    }

    private void validate(Shipment shipment) {
        if (shipment == null) {
            throw new IllegalArgumentException("Відправлення не може бути null");
        }
        if (shipment.getDescription() == null || shipment.getDescription().isBlank()) {
            throw new IllegalArgumentException("Опис відправлення є обов'язковим");
        }
        if (shipment.getWeight() <= 0) {
            throw new IllegalArgumentException("Вага відправлення має бути більшою за 0");
        }
    }
}