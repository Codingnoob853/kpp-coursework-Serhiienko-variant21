package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ClientService {
    private static final Logger LOG = Logger.getLogger(ClientService.class.getName());
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
        LOG.info("Client added");
    }

    public void update(Client client) throws SQLException {
        if (client == null || client.getId() <= 0) {
            throw new IllegalArgumentException("Invalid client ID for update");
        }
        validate(client);
        dao.update(client);
        LOG.info("Client updated");
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
        LOG.info("Deleted client with ID " + id);
    }

    private void validate(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client must not be null");
        }
        if (client.getFullName() == null || client.getFullName().isBlank()) {
            throw new IllegalArgumentException("Client full name is required");
        }
        if (client.getPhone() == null || client.getPhone().isBlank()) {
            throw new IllegalArgumentException("Client phone is required");
        }
    }
}