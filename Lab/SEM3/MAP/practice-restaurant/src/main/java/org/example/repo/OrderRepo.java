package org.example.repo;

import org.example.domain.DataBaseConfig;
import org.example.domain.MenuItem;
import org.example.domain.Order;
import org.example.domain.OrderStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepo extends DatabaseConnection {
    private final MenuItemRepo menuItemRepo;

    public OrderRepo(String url, String user, String password, MenuItemRepo menuItemRepo) {
        super(url, user, password);
        this.menuItemRepo = menuItemRepo;
    }

    public OrderRepo(DataBaseConfig config, MenuItemRepo menuItemRepo) {
        super(config);
        this.menuItemRepo = menuItemRepo;
    }

    public void add(Order order){
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try {
                long orderId = insertOrder(connection,order);
                insertItem(connection, orderId, order.getMenuItems());

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long insertOrder(Connection connection, Order order){
        String sql = "INSERT INTO orders (table_id, date, status) values (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, order.getTableId());
            ps.setTimestamp(2, Timestamp.valueOf(order.getDate()));
            ps.setString(3, order.getStatus().toString());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                } else {
                    throw new SQLException("Failed to get generated ID for message");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertItem(Connection connection, Long orderId,  List<MenuItem> items){
        String sql = "INSERT INTO order_items (order_id, item_id) VALUES (? , ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (MenuItem item : items) {
                ps.setLong(1, orderId);
                ps.setLong(2, item.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Order> getAll() {
        String sql = "SELECT * FROM orders ORDER BY date DESC ";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            List<Order> items = new ArrayList<>();
            while (rs.next()) {
                items.add(new Order(
                        rs.getLong("id"),
                        rs.getLong("table_id"),
                        fetchOrderItems(connection, rs.getLong("id")),
                        rs.getTimestamp("date").toLocalDateTime(),
                        OrderStatus.valueOf(rs.getString("status"))
                ));


            }
            return items;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<MenuItem> fetchOrderItems(Connection conn, long orderId) {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT item_id FROM order_items WHERE order_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MenuItem item = menuItemRepo.find(rs.getLong("item_id"));
                    if (item != null) items.add(item);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return items;
    }
}
