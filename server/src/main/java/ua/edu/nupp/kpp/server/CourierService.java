package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;

public class CourierService {
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
    }

    public void update(Courier courier) throws SQLException {
        if (courier == null || courier.getId() <= 0) {
            throw new IllegalArgumentException("Некоректний ID кур'єра для оновлення");
        }
        validate(courier);
        dao.update(courier);
    }

    public void updateStatus(long id, String status) throws SQLException {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Статус кур'єра не може бути порожнім");
        }
        dao.updateStatus(id, status);
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
    }

    private void validate(Courier courier) {
        if (courier == null) {
            throw new IllegalArgumentException("Кур'єр не може бути null");
        }
        if (courier.getFullName() == null || courier.getFullName().isBlank()) {
            throw new IllegalArgumentException("ПІБ кур'єра є обов'язковим");
        }
        if (courier.getPhone() == null || courier.getPhone().isBlank()) {
            throw new IllegalArgumentException("Телефон кур'єра є обов'язковим");
        }
    }
}