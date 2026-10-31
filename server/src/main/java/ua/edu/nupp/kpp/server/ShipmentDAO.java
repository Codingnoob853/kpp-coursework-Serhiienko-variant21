package ua.edu.nupp.kpp.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShipmentDAO {

    public void add(Shipment shipment) throws SQLException {
        String sql = "INSERT INTO shipment (tracking_number, weight, declared_value, description) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shipment.getTrackingNumber());
            ps.setDouble(2, shipment.getWeight());
            ps.setDouble(3, shipment.getDeclaredValue());
            ps.setString(4, shipment.getDescription());

            ps.executeUpdate();
        }
    }

    public List<Shipment> getAll() throws SQLException {
        String sql = "SELECT id, tracking_number, weight, declared_value, description FROM shipment ORDER BY id";
        List<Shipment> shipments = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Shipment shipment = new Shipment();
                shipment.setId(rs.getLong("id"));
                shipment.setTrackingNumber(rs.getString("tracking_number"));
                shipment.setWeight(rs.getDouble("weight"));
                shipment.setDeclaredValue(rs.getDouble("declared_value"));
                shipment.setDescription(rs.getString("description"));

                shipments.add(shipment);
            }
        }
        return shipments;
    }

    public Shipment getById(long id) throws SQLException {
        String sql = "SELECT id, tracking_number, weight, declared_value, description FROM shipment WHERE id = ?";
        Shipment shipment = null;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    shipment = new Shipment();
                    shipment.setId(rs.getLong("id"));
                    shipment.setTrackingNumber(rs.getString("tracking_number"));
                    shipment.setWeight(rs.getDouble("weight"));
                    shipment.setDeclaredValue(rs.getDouble("declared_value"));
                    shipment.setDescription(rs.getString("description"));
                }
            }
        }
        return shipment;
    }

    public void update(Shipment shipment) throws SQLException {
        String sql = "UPDATE shipment SET tracking_number = ?, weight = ?, declared_value = ?, description = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shipment.getTrackingNumber());
            ps.setDouble(2, shipment.getWeight());
            ps.setDouble(3, shipment.getDeclaredValue());
            ps.setString(4, shipment.getDescription());
            ps.setLong(5, shipment.getId());

            ps.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM shipment WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}