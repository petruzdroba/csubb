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

    @Override
    public Collection<Friendship> getPage(int offset, int limit) {
        String sql = "SELECT * FROM friendships ORDER BY friendship_id LIMIT ? OFFSET ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            stmt.setInt(2, offset);

            ResultSet rs = stmt.executeQuery();

            List<Friendship> values = new ArrayList<>();
            while (rs.next()) {
                Friendship entity = mapResultSetToEntity(rs);
                values.add(entity);
            }

            return values;

        } catch (SQLException e) {
            throw new RepositoryException("Failed to fetch paginated values: " + e.getMessage());
        }
    }

    @Override
    public int pageCount(int pageSize) {
        String sql = "SELECT COUNT(*) AS total FROM friendships";

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                int totalRows = rs.getInt("total");
                return (int) Math.ceil((double) totalRows / pageSize);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get page count: " + e.getMessage());
        }

        return 0;
    }




    private List<String> fetchFriendshipKeys(String sql, long userId, Integer limit, Integer offset) {
        List<String> keys = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setLong(2, userId);
            if (limit != null) ps.setInt(3, limit);
            if (offset != null) ps.setInt(4, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    keys.add(rs.getString("friendship_id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return keys;
    }

    public Collection<String> getKeysByUser(long userId) {
        String sql = "SELECT friendship_id FROM friendships WHERE user_id1=? OR user_id2=? ORDER BY friendship_id";
        return fetchFriendshipKeys(sql, userId, null, null);
    }

    public Collection<String> getKeysPageByUser(long userId, int offset, int limit) {
        String sql = "SELECT friendship_id FROM friendships WHERE user_id1=? OR user_id2=? ORDER BY friendship_id LIMIT ? OFFSET ?";
        return fetchFriendshipKeys(sql, userId, limit, offset);
    }

    public Collection<Friendship> getFriendshipsPageByUser(long userId, int offset, int limit) {
        String sql = "SELECT * FROM friendships WHERE user_id1=? OR user_id2=? ORDER BY friendship_id LIMIT ? OFFSET ?";
        List<Friendship> friendships = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setLong(2, userId);
            ps.setInt(3, limit);
            ps.setInt(4, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    friendships.add(mapResultSetToEntity(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return friendships;
    }

    public int pageCountByUser(long userId, int pageSize) {
        String sql = "SELECT COUNT(*) AS total FROM friendships WHERE user_id1=? OR user_id2=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setLong(2, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int total = rs.getInt("total");
                    return (int) Math.ceil((double) total / pageSize);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

}
