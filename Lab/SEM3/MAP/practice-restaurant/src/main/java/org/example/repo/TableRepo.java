package org.example.repo;

import org.example.domain.DataBaseConfig;
import org.example.domain.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableRepo extends DatabaseConnection {
    public TableRepo(String url, String user, String password) {
        super(url, user, password);
    }

    public TableRepo(DataBaseConfig config) {
        super(config);
    }

    public List<Table> getAll() {
        String sql = "SELECT * FROM tables";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            List<Table> tables = new ArrayList<>();

            while(rs.next()){
                Table table = new Table(rs.getLong("id"));
                tables.add(table);
            }
            return tables;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
