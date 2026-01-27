package org.example.repo;

import org.example.domain.DataBaseConfig;
import org.example.domain.MenuItem;
import org.example.domain.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemRepo extends DatabaseConnection {
    public MenuItemRepo(String url, String user, String password) {
        super(url, user, password);
    }

    public MenuItemRepo(DataBaseConfig config) {
        super(config);
    }

    public List<MenuItem> getAll() {
        String sql = "SELECT * FROM menu_items ORDER BY category";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            List<MenuItem> items = new ArrayList<>();
            while (rs.next()) {
                items.add(new MenuItem(
                        rs.getLong("id"),
                        rs.getString("category"),
                        rs.getString("item"),
                        rs.getFloat("price"),
                        rs.getString("currency")
                ));
            }
            return items;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getCategories() {
        String sql = "SELECT DISTINCT category FROM menu_items";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            List<String> items = new ArrayList<>();
            while (rs.next()) {
                items.add(rs.getString("category"));
            }
            return items;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<MenuItem> getByCategory(String category) {
        String sql = "SELECT * FROM menu_items WHERE category=?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, category);

            try (ResultSet rs = ps.executeQuery()) {
                List<MenuItem> items = new ArrayList<>();
                while (rs.next()) {
                    items.add(new MenuItem(
                            rs.getLong("id"),
                            rs.getString("category"),
                            rs.getString("item"),
                            rs.getFloat("price"),
                            rs.getString("currency")
                    ));
                }
                return items;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public MenuItem find(Long key) {
        String sql = "SELECT * FROM menu_items WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, key);

            try (ResultSet rs = ps.executeQuery()) {
                MenuItem item = null;
                while (rs.next()) {
                    item = new MenuItem(
                            rs.getLong("id"),
                            rs.getString("category"),
                            rs.getString("item"),
                            rs.getFloat("price"),
                            rs.getString("currency")
                    );
                }
                return item;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
