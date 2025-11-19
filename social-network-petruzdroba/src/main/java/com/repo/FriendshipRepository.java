package com.repo;

import com.domain.DataBaseConfig;
import com.domain.Friendship;
import com.exceptions.RepositoryException;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FriendshipRepository extends AbstractDatabaseRepository<String, Friendship> {

    public FriendshipRepository(String url, String user, String password) {
        super(url, user, password);
    }

    public FriendshipRepository(DataBaseConfig config) {
        super(config);
    }

    @Override
    public void add(String key, Friendship entity) throws SQLException {
        String sql = "INSERT INTO friendships (user_id1, user_id2, friendship_id) VALUES (?, ?, ?)";

        try(Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setLong(1, entity.getUserId1());
            ps.setLong(2, entity.getUserId2());
            ps.setString(3, key);

            ps.executeUpdate();
        }
    }

    @Override
    public void remove(String key) throws SQLException {
        String sql = "DELETE FROM friendships WHERE friendship_id = ?";
        try(Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, key);
            ps.executeUpdate();
        }
    }

    @Override
    public void modify(String key, Friendship entity) throws SQLException {
        throw new UnsupportedOperationException("Friendships cannot be modified, only added or removed.");
    }

    @Override
    public Friendship find(String key) throws SQLException {
        String sql = "SELECT * FROM friendships WHERE friendship_id=?";

        try(Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, key);

            try(ResultSet rs= ps.executeQuery()){

                if (!rs.next()) {
                    return null;
                }

                long user1 = rs.getLong("user_id1");
                long user2 = rs.getLong("user_id2");

                return new Friendship(user1, user2);
            }
        }
    }

    private Friendship mapResultSetToEntity(ResultSet rs) throws SQLException {
        long user1 = rs.getLong("user_id1");
        long user2 = rs.getLong("user_id2");

        return new Friendship(user1, user2);
    }

    @Override
    public Collection<Friendship> getAll() {
        String sql = "SELECT * FROM friendships";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<Friendship> values = new ArrayList<>();
            while (rs.next()) {
                Friendship entity = mapResultSetToEntity(rs);
                values.add(entity);
            }
            return values;

        } catch (SQLException e) {
            throw new RepositoryException("Failed to fetch values from DB: " + e.getMessage());
        }
    }

    @Override
    public Collection<String> getKeys() {
        String sql = "SELECT id FROM users";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<String> keys = new ArrayList<>();
            while (rs.next()) {
                keys.add(rs.getString("friendship_id"));
            }
            return keys;

        } catch (SQLException e) {
            throw new RepositoryException("Failed to fetch keys from DB: " + e.getMessage());
        }
    }
}
