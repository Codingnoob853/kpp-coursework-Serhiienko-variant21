package ua.edu.nupp.kpp.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourierDAO {

    public void add(Courier courier) throws SQLException {
        String sql = "INSERT INTO courier (full_name, phone, vehicle_type, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, courier.getFullName());
            ps.setString(2, courier.getPhone());
            ps.setString(3, courier.getVehicleType());
            ps.setString(4, courier.getStatus());

            ps.executeUpdate();
        }
    }

    public List<Courier> getAll() throws SQLException {
        String sql = "SELECT id, full_name, phone, vehicle_type, status FROM courier ORDER BY id";
        List<Courier> couriers = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Courier courier = new Courier();
                courier.setId(rs.getLong("id"));
                courier.setFullName(rs.getString("full_name"));
                courier.setPhone(rs.getString("phone"));
                courier.setVehicleType(rs.getString("vehicle_type"));
                courier.setStatus(rs.getString("status"));

                couriers.add(courier);
            }
        }
        return couriers;
    }

    public Courier getById(long id) throws SQLException {
        String sql = "SELECT id, full_name, phone, vehicle_type, status FROM courier WHERE id = ?";
        Courier courier = null;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    courier = new Courier();
                    courier.setId(rs.getLong("id"));
                    courier.setFullName(rs.getString("full_name"));
                    courier.setPhone(rs.getString("phone"));
                    courier.setVehicleType(rs.getString("vehicle_type"));
                    courier.setStatus(rs.getString("status"));
                }
            }
        }
        return courier;
    }

    public void update(Courier courier) throws SQLException {
        String sql = "UPDATE courier SET full_name = ?, phone = ?, vehicle_type = ?, status = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, courier.getFullName());
            ps.setString(2, courier.getPhone());
            ps.setString(3, courier.getVehicleType());
            ps.setString(4, courier.getStatus());
            ps.setLong(5, courier.getId());

            ps.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM courier WHERE id = ?";

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