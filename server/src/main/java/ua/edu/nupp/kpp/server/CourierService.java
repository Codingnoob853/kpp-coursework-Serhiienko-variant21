package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class CourierService {
    private static final Logger LOG = Logger.getLogger(CourierService.class.getName());
    private final CourierDAO dao = new CourierDAO();

    public List<Courier> getAll() throws SQLException {
        return dao.getAll();
    }

    public Courier getById(long id) throws SQLException {
        return dao.getById(id);
    }

    public void add(Courier courier) throws SQLException {
        validate(courier);
        dao.add(courier);
        LOG.info("Courier added");
    }

    public void update(Courier courier) throws SQLException {
        if (courier == null || courier.getId() <= 0) {
            throw new IllegalArgumentException("Invalid courier ID for update");
        }
        validate(courier);
        dao.update(courier);
        LOG.info("Courier updated");
    }

    public void updateStatus(long id, String status) throws SQLException {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Courier status must not be blank");
        }
        dao.updateStatus(id, status);
        LOG.info("Updated courier ID " + id + " status to " + status);
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
        LOG.info("Deleted courier with ID " + id);
    }

    private void validate(Courier courier) {
        if (courier == null) {
            throw new IllegalArgumentException("Courier must not be null");
        }
        if (courier.getFullName() == null || courier.getFullName().isBlank()) {
            throw new IllegalArgumentException("Courier full name is required");
        }
        if (courier.getPhone() == null || courier.getPhone().isBlank()) {
            throw new IllegalArgumentException("Courier phone is required");
        }
    }
}