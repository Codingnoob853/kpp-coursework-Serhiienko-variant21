package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;

public class ClientService {
    private final ClientDAO dao = new ClientDAO();

    public List<Client> getAll() throws SQLException {
        return dao.getAll();
    }

    public Client getById(long id) throws SQLException {
        return dao.getById(id);
    }

    public void add(Client client) throws SQLException {
        validate(client);
        dao.add(client);
    }

    public void update(Client client) throws SQLException {
        if (client == null || client.getId() <= 0) {
            throw new IllegalArgumentException("Некоректний ID клієнта для оновлення");
        }
        validate(client);
        dao.update(client);
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
    }

    private void validate(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Клієнт не може бути null");
        }
        if (client.getFullName() == null || client.getFullName().isBlank()) {
            throw new IllegalArgumentException("ПІБ клієнта є обов'язковим");
        }
        if (client.getPhone() == null || client.getPhone().isBlank()) {
            throw new IllegalArgumentException("Телефон клієнта є обов'язковим");
        }
    }
}