package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ShipmentService {
    private static final Logger LOG = Logger.getLogger(ShipmentService.class.getName());
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
        LOG.info("Додано відправлення");
    }

    public void update(Shipment shipment) throws SQLException {
        if (shipment == null || shipment.getId() <= 0) {
            throw new IllegalArgumentException("Некоректний ID відправлення для оновлення");
        }
        validate(shipment);
        dao.update(shipment);
        LOG.info("Оновлено відправлення");
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
        LOG.info("Видалено відправлення з ID " + id);
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