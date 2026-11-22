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
        LOG.info("Shipment added");
    }

    public void update(Shipment shipment) throws SQLException {
        if (shipment == null || shipment.getId() <= 0) {
            throw new IllegalArgumentException("Invalid shipment ID for update");
        }
        validate(shipment);
        dao.update(shipment);
        LOG.info("Shipment updated");
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
        LOG.info("Deleted shipment with ID " + id);
    }

    private void validate(Shipment shipment) {
        if (shipment == null) {
            throw new IllegalArgumentException("Shipment must not be null");
        }
        if (shipment.getDescription() == null || shipment.getDescription().isBlank()) {
            throw new IllegalArgumentException("Shipment description is required");
        }
        if (shipment.getWeight() <= 0) {
            throw new IllegalArgumentException("Shipment weight must be greater than 0");
        }
    }
}