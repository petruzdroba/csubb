package com.repo;

import com.domain.DataBaseConfig;
import com.domain.ProfilePicture;
import com.exceptions.RepositoryException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProfilePictureRepository extends AbstractDatabaseRepository<Long, ProfilePicture> {
    public ProfilePictureRepository(String url, String user, String password) {
        super(url, user, password);
    }

    public ProfilePictureRepository(DataBaseConfig config) {
        super(config);
    }

    @Override
    public void add(Long key, ProfilePicture entity) throws SQLException {
        String sql ="INSERT INTO profile_pictures (user_id, image, content_type, uploaded_at) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, key);
            ps.setBytes(2, entity.getImage());
            ps.setString(3, entity.getContentType());
            ps.setTimestamp(4, Timestamp.valueOf(entity.getUploadedAt()));

            ps.executeUpdate();
        }
    }

    @Override
    public void remove(Long key) throws SQLException {
        String sql = "DELETE FROM profile_pictures WHERE user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, key);
            ps.executeUpdate();
        }
    }

    @Override
    public void modify(Long key, ProfilePicture entity) throws SQLException {
        String sql = """
            UPDATE profile_pictures
            SET image = ?, content_type = ?, uploaded_at = ?
            WHERE user_id = ?
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBytes(1, entity.getImage());
            ps.setString(2, entity.getContentType());
            ps.setTimestamp(3, Timestamp.valueOf(entity.getUploadedAt()));
            ps.setLong(4, key);

            ps.executeUpdate();
        }
    }

    @Override
    public ProfilePicture find(Long key) throws SQLException {
        String sql = "SELECT * FROM profile_pictures WHERE user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, key);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return mapResultSetToEntity(rs);
            }
        }
    }

    private ProfilePicture mapResultSetToEntity(ResultSet rs) throws SQLException {
        long userId = rs.getLong("user_id");
        byte[] image = rs.getBytes("image");
        String contentType = rs.getString("content_type");
        LocalDateTime uploadedAt = rs.getTimestamp("uploaded_at").toLocalDateTime();

        return new ProfilePicture(userId, image, contentType, uploadedAt);
    }

    @Override
    public Collection<ProfilePicture> getAll() {
        String sql = "SELECT * FROM profile_pictures";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<ProfilePicture> values = new ArrayList<>();
            while (rs.next()) {
                values.add(mapResultSetToEntity(rs));
            }
            return values;

        } catch (SQLException e) {
            throw new RepositoryException("Failed to fetch profile pictures: " + e.getMessage());
        }
    }

    @Override
    public Collection<Long> getKeys() {
        String sql = "SELECT user_id FROM profile_pictures";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<Long> keys = new ArrayList<>();
            while (rs.next()) {
                keys.add(rs.getLong("user_id"));
            }
            return keys;

        } catch (SQLException e) {
            throw new RepositoryException("Failed to fetch profile picture keys: " + e.getMessage());
        }
    }

    @Override
    public Collection<ProfilePicture> getPage(int offset, int limit) {
        String sql = """
            SELECT * FROM profile_pictures
            ORDER BY user_id
            LIMIT ? OFFSET ?
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ps.setInt(2, offset);

            ResultSet rs = ps.executeQuery();

            List<ProfilePicture> values = new ArrayList<>();
            while (rs.next()) {
                values.add(mapResultSetToEntity(rs));
            }

            return values;

        } catch (SQLException e) {
            throw new RepositoryException("Failed to fetch paginated profile pictures: " + e.getMessage());
        }
    }

    @Override
    public int pageCount(int pageSize) {
        String sql = "SELECT COUNT(*) AS total FROM profile_pictures";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                int total = rs.getInt("total");
                return (int) Math.ceil((double) total / pageSize);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get page count: " + e.getMessage(), e);
        }

        return 0;
    }
}
/*
* CREATE TABLE profile_pictures (
    user_id BIGINT PRIMARY KEY,
    image BYTEA NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    uploaded_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_profile_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
);

* */