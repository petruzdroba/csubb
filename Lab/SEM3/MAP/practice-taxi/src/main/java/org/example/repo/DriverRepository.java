package org.example.repo;

import org.example.domain.DataBaseConfig;
import org.example.domain.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DriverRepository extends DatabaseConnection implements RepositoryPaginated<Long, Driver> {

    public DriverRepository(String url, String user, String password) {
        super(url, user, password);
    }

    public DriverRepository(DataBaseConfig config) {
        super(config);
    }

    @Override
    public List<Driver> getPage(int limit, int offset) {
        return List.of();
    }

    @Override
    public int pageCount(int pageSize) {
        return 0;
    }

    @Override
    public void add(Long key, Driver entity) {

    }

    @Override
    public Driver find(Long key) {
        return null;
    }

    @Override
    public void update(Long key, Driver entity) {

    }

    @Override
    public List<Driver> getAll() {
        String sql = "SELECT * FROM drivers";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<Driver> values = new ArrayList<>();
            while (rs.next()) {
                Driver entity = new Driver(rs.getLong("id"), rs.getString("name"));
                values.add(entity);
            }
            return values;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch values from DB: " + e.getMessage());
        }
    }

    @Override
    public List<Long> getKeys() {
        return List.of();
    }
}
