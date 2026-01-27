package org.example.repo;

import org.example.domain.DataBaseConfig;
import org.example.domain.Order;
import org.example.domain.Status;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository extends DatabaseConnection implements RepositoryPaginated<Long, Order> {
    public OrderRepository(String url, String user, String password) {
        super(url, user, password);
    }

    public OrderRepository(DataBaseConfig config) {
        super(config);
    }

    @Override
    public List<Order> getPage(int limit, int offset) {
        String sql = "SELECT * FROM orders ORDER BY driverId LIMIT ? OFFSET ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ps.setInt(2, offset);

            ResultSet rs = ps.executeQuery();

            List<Order> values = new ArrayList<>();
            while (rs.next()) {
                values.add(mapToOrder(rs));
            }
            return values;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch paginated profile pictures: " + e.getMessage());
        }
    }

    public List<Order> getPageFiltered(int limit, int offset, Status status, Long driverId) {
        String sql = "SELECT * FROM orders WHERE status = ? AND driver_id = ? ORDER BY driver_id LIMIT ? OFFSET ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.toString());

            if (driverId == null) {
                ps.setNull(2, java.sql.Types.BIGINT);
            } else {
                ps.setLong(2, driverId);
            }

            ps.setInt(3, limit);
            ps.setInt(4, offset);

            ResultSet rs = ps.executeQuery();

            List<Order> orders = new ArrayList<>();
            while (rs.next()) {
                Long id = rs.getLong("id");

                Long driverId2 = rs.getLong("driver_id");
                if (rs.wasNull()) driverId = null;

                Status status2 = Status.valueOf(rs.getString("status"));

                LocalDateTime startDate = rs.getTimestamp("start_date").toLocalDateTime();
                LocalDateTime endDate = rs.getTimestamp("end_date").toLocalDateTime();

                String pickupAddress = rs.getString("pickup_address");
                String destinationAddress = rs.getString("destination_address");
                String clientName = rs.getString("client_name");

                orders.add(new Order(id, driverId2, status2, startDate, endDate, pickupAddress, destinationAddress, clientName));
            }
            return orders;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch paginated orders: " + e.getMessage(), e);
        }
    }


    public List<Order> getPageAll(Status status, Long driverId) {
        String sql = "SELECT * FROM orders WHERE status = ? AND driver_id = ? ORDER BY driver_id ";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.toString());
            ps.setLong(2, driverId);

            ResultSet rs = ps.executeQuery();

            List<Order> values = new ArrayList<>();
            while (rs.next()) {
                values.add(mapToOrder(rs));
            }
            return values;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch paginated profile pictures: " + e.getMessage());
        }
    }

    public int pageCountFiltered(int pageSize, Status status, Long driverId) {
        return (int) Math.ceil((double) getPageAll(status, driverId).size() / pageSize);
    }

    private static Order mapToOrder(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");

        Long driverId = rs.getLong("driver_id");
        if (rs.wasNull()) driverId = null;

        Status status = Status.valueOf(rs.getString("status"));

        LocalDateTime startDate = rs.getTimestamp("start_date").toLocalDateTime();
        LocalDateTime endDate = rs.getTimestamp("end_date").toLocalDateTime();

        String pickupAddress = rs.getString("pickup_address");
        String destinationAddress = rs.getString("destination_address");
        String clientName = rs.getString("client_name");

        return new Order(id, driverId, status, startDate, endDate, pickupAddress, destinationAddress, clientName);
    }


    @Override
    public int pageCount(int pageSize) {
        return 0;
    }

    @Override
    public void add(Long key, Order entity) {
        String sql = "INSERT INTO orders (driver_id, status, start_date, end_date, pickup_address, destination_address, client_name) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (entity.getDriverId() == null) {
                ps.setNull(1, java.sql.Types.BIGINT);
            } else {
                ps.setLong(1, entity.getDriverId());
            }
            ps.setString(2, entity.getStatus().toString());
            ps.setTimestamp(3, Timestamp.valueOf(entity.getStartDate()));
            ps.setTimestamp(4, Timestamp.valueOf(entity.getEndDate()));
            ps.setString(5, entity.getPickupAddress());
            ps.setString(6, entity.getDestinationAddress());
            ps.setString(7, entity.getClientName());

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    entity.setId(keys.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order find(Long key) {
        String sql = "SELECT * FROM orders WHERE id=?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, key);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return null;
                }

                return mapToOrder(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Long key, Order entity) {
        String sql = "UPDATE orders SET status = ?,  end_date = ? WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getStatus().toString());
            ps.setTimestamp(2, Timestamp.valueOf(entity.getEndDate()));
            ps.setLong(3, entity.getId());


            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateDriver(Order entity, long driverId) {
        String sql = "UPDATE orders SET driver_id = ?, status =?  WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, driverId);
            ps.setString(2, Status.IN_PROGRESS.name());
            ps.setLong(3, entity.getId());


            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> getAll() {

        return List.of();
    }

    @Override
    public List<Long> getKeys() {
        return List.of();
    }
}
