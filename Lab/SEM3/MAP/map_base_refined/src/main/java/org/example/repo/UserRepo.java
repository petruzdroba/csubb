package org.example.repo;

import org.example.domain.Role;
import org.example.domain.User;
import org.example.domain.DataBaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepo extends DatabaseConnection {
    public UserRepo(String url, String user, String password) {
        super(url, user, password);
    }

    public UserRepo(DataBaseConfig config) {
        super(config);
    }

    public User find(Long key) {
        String sql = "SELECT * FROM users WHERE id=?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, key);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return null;
                }

                return new User(rs.getLong("id"), Role.valueOf(rs.getString("role")
                ), rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public User find(String key) {
        String sql = "SELECT * FROM users WHERE username=?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, key);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return null;
                }

                return new User(rs.getLong("id"), Role.valueOf(rs.getString("role")
                ), rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
