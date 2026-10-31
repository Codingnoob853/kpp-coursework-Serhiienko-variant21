package ua.edu.nupp.kpp.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public void add(Client client) throws SQLException {
        String sql = "INSERT INTO client (full_name, phone, email, address) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, client.getFullName());
            ps.setString(2, client.getPhone());
            ps.setString(3, client.getEmail());
            ps.setString(4, client.getAddress());

            ps.executeUpdate();
        }
    }

    public List<Client> getAll() throws SQLException {
        String sql = "SELECT id, full_name, phone, email, address FROM client ORDER BY id";
        List<Client> clients = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getLong("id"));
                client.setFullName(rs.getString("full_name"));
                client.setPhone(rs.getString("phone"));
                client.setEmail(rs.getString("email"));
                client.setAddress(rs.getString("address"));

                clients.add(client);
            }
        }
        return clients;
    }

    public Client getById(long id) throws SQLException {
        String sql = "SELECT id, full_name, phone, email, address FROM client WHERE id = ?";
        Client client = null;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    client = new Client();
                    client.setId(rs.getLong("id"));
                    client.setFullName(rs.getString("full_name"));
                    client.setPhone(rs.getString("phone"));
                    client.setEmail(rs.getString("email"));
                    client.setAddress(rs.getString("address"));
                }
            }
        }
        return client;
    }

    public void update(Client client) throws SQLException {
        String sql = "UPDATE client SET full_name = ?, phone = ?, email = ?, address = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, client.getFullName());
            ps.setString(2, client.getPhone());
            ps.setString(3, client.getEmail());
            ps.setString(4, client.getAddress());
            ps.setLong(5, client.getId());

            ps.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM client WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public void updateStatus(long id, String status) throws SQLException {
        String sql = "UPDATE courier SET status = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setLong(2, id);

            ps.executeUpdate();
        }
    }
}