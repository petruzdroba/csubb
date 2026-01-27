package org.example.repo;

import org.example.domain.Book;
import org.example.domain.DataBaseConfig;
import org.example.domain.Patron;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatronRepo extends DatabaseConnection{
    public PatronRepo(String url, String user, String password) {
        super(url, user, password);
    }

    public PatronRepo(DataBaseConfig config) {
        super(config);
    }

    public List<Patron> getAll(){

        String sql = "SELECT * FROM patrons";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<Patron> values = new ArrayList<>();
            while (rs.next()) {
                Patron entity = new Patron( rs.getLong("id"), rs.getString("name"));
                values.add(entity);
            }
            return values;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch values from DB: " + e.getMessage());
        }
    }

    public Patron find(Long key) {
        String sql = "SELECT * FROM patrons WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setLong(1, key);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                return new Patron(
                        key,
                        rs.getString("name")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
