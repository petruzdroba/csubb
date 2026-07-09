package com.zdroba.mpp.repository;

import com.zdroba.mpp.entity.User;
import com.zdroba.mpp.utils.DbConnection;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@Repository
public class UserRepository implements IUserRepository {

    private final DbConnection dbConnection;

    public UserRepository(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public User find(String email) {

        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException("Error finding user by email", e);
        }
    }

    @Override
    public User find(Long id) {

        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException("Error finding user by id", e);
        }
    }

    @Override
    public Long save(User user) {

        String sql = "INSERT INTO users(name, email, password) VALUES (?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();

            if (keys.next()) {
                return keys.getLong(1);
            }

            return -1L;

        } catch (Exception e) {
            throw new RuntimeException("Error saving user", e);
        }
    }
}