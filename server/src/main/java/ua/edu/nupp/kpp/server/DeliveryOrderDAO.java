package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryOrderDAO {

    public void add(DeliveryOrder order) throws SQLException {
        String sql = "INSERT INTO delivery_order (shipment_id, client_id, courier_id, order_date, status, total_cost) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, order.getShipmentId());
            ps.setLong(2, order.getClientId());
            ps.setLong(3, order.getCourierId());
            ps.setString(4, order.getOrderDate());
            ps.setString(5, order.getStatus());
            ps.setDouble(6, order.getTotalCost());
            ps.executeUpdate();
        }
    }

    public List<DeliveryOrder> getAll() throws SQLException {
        String sql = "SELECT id, shipment_id, client_id, courier_id, order_date, status, total_cost FROM delivery_order ORDER BY id";
        List<DeliveryOrder> orders = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DeliveryOrder order = new DeliveryOrder(
                        rs.getLong("id"),
                        rs.getLong("client_id"),
                        rs.getLong("courier_id"),
                        rs.getLong("shipment_id"),
                        rs.getString("order_date"),
                        rs.getString("status"),
                        rs.getDouble("total_cost")
                );
                orders.add(order);
            }
        }
        return orders;
    }

    public void updateStatus(long id, String status) throws SQLException {
        String sql = "UPDATE delivery_order SET status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }

    public DeliveryOrder getById(long id) throws SQLException {
        String sql = "SELECT id, shipment_id, client_id, courier_id, order_date, status, total_cost FROM delivery_order WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new DeliveryOrder(
                            rs.getLong("id"),
                            rs.getLong("client_id"),
                            rs.getLong("courier_id"),
                            rs.getLong("shipment_id"),
                            rs.getString("order_date"),
                            rs.getString("status"),
                            rs.getDouble("total_cost")
                    );
                }
            }
        }
        return null;
    }

    public void update(DeliveryOrder order) throws SQLException {
        String sql = "UPDATE delivery_order SET shipment_id = ?, client_id = ?, courier_id = ?, order_date = ?, status = ?, total_cost = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, order.getShipmentId());
            ps.setLong(2, order.getClientId());
            ps.setLong(3, order.getCourierId());
            ps.setString(4, order.getOrderDate());
            ps.setString(5, order.getStatus());
            ps.setDouble(6, order.getTotalCost());
            ps.setLong(7, order.getId());
            ps.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM delivery_order WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}