package com.repo;

import com.domain.DataBaseConfig;
import com.domain.Notification;
import com.domain.User;
import com.exceptions.NotLoggedIn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NotificationRepository extends AbstractDatabaseRepository<Long, Notification> {
    private final UserRepository userRepo;

    public NotificationRepository(String url, String user, String password, UserRepository userRepo) {
        super(url, user, password);
        this.userRepo = userRepo;
    }

    public NotificationRepository(DataBaseConfig config, UserRepository userRepo) {
        super(config);
        this.userRepo = userRepo;
    }

    @Override
    public void add(Long key, Notification entity) throws SQLException {
        String sql = "INSERT INTO notifications (to_user_id, data, text, read) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, entity.getTo().getId());
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(entity.getData()));
            ps.setString(3, entity.getText());
            ps.setBoolean(4, entity.isRead());

            ps.executeUpdate();
        }
    }

    @Override
    public void remove(Long key) throws SQLException {
        String sql = "DELETE FROM notifications WHERE id=?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, key);
            ps.executeUpdate();
        }
    }

    @Override
    public void modify(Long key, Notification entity) throws SQLException {
        String sql = "UPDATE notifications SET read=? WHERE id=?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setBoolean(1, entity.isRead());
            ps.setLong(2, key);

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new SQLException("No notification found with id " + key);
            }
        }
    }

    @Override
    public Notification find(Long key) throws SQLException {
        String sql = "SELECT * FROM notifications WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, key);

            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                long notifId = rs.getLong("id");
                long toUserId = rs.getLong("to_user_id");
                LocalDateTime date = rs.getTimestamp("data").toLocalDateTime();
                String text = rs.getString("text");
                boolean read = rs.getBoolean("read");

                com.domain.User toUser = userRepo.find(toUserId);
                if (toUser == null) {
                    throw new SQLException("Recipient not found for notification " + notifId);
                }

                Notification notification = new Notification();
                notification.setId(notifId);
                notification.setTo(toUser);
                notification.setData(date);
                notification.setText(text);
                notification.setRead(read);

                return notification;
            }
        }
    }

    private List<Notification> fetchNotifications(String sql, long userId, Integer limit, Integer offset) {
        List<Notification> notifications = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, userId);
            if (limit != null) ps.setInt(2, limit);
            if (offset != null) ps.setInt(3, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    notifications.add(find(rs.getLong("id")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return notifications;
    }

    public Collection<Notification> getAllNotifications(User user) {
        if (user == null) throw new NotLoggedIn("User is not logged in");

        String sql = "SELECT * FROM notifications WHERE to_user_id=? ORDER BY data DESC";

        return fetchNotifications(sql, user.getId(), null, null);
    }

    public Collection<Notification> getUnreadNotifications(User user) {
        if (user == null) throw new NotLoggedIn("User is not logged in");

        String sql = "SELECT * FROM notifications WHERE to_user_id=? AND read=false ORDER BY data DESC";

        return fetchNotifications(sql, user.getId(), null, null);
    }

    public Collection<Notification> getAllNotificationsPage(User user, int offset, int limit) {
        if (user == null) throw new NotLoggedIn("User is not logged in");

        String sql = "SELECT * FROM notifications WHERE to_user_id=? ORDER BY data DESC LIMIT ? OFFSET ?";

        return fetchNotifications(sql, user.getId(), limit, offset);
    }

    public Collection<Notification> getUnreadNotificationsPage(User user, int offset, int limit) {
        if (user == null) throw new NotLoggedIn("User is not logged in");

        String sql = "SELECT * FROM notifications WHERE to_user_id=? AND read=false ORDER BY data DESC LIMIT ? OFFSET ?";

        return fetchNotifications(sql, user.getId(), limit, offset);
    }

    private List<Long> fetchNotificationKeys(String sql, long userId, Integer limit, Integer offset) {
        List<Long> keys = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, userId);
            if (limit != null) ps.setInt(2, limit);
            if (offset != null) ps.setInt(3, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    keys.add(rs.getLong("id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return keys;
    }

    public Collection<Long> getAllNotificationKeys(User user) {
        if (user == null) throw new NotLoggedIn("User is not logged in");

        String sql = "SELECT * FROM notifications WHERE to_user_id=? ORDER BY data DESC";
        return fetchNotificationKeys(sql, user.getId(), null, null);
    }

    public Collection<Long> getUnreadNotificationKeys(User user) {
        if (user == null) throw new NotLoggedIn("User is not logged in");

        String sql = "SELECT * FROM notifications WHERE to_user_id=? AND read=false ORDER BY data DESC";
        return fetchNotificationKeys(sql, user.getId(), null, null);
    }

    public Collection<Long> getAllNotificationKeysPage(User user, int offset, int limit) {
        if (user == null) throw new NotLoggedIn("User is not logged in");

        String sql = "SELECT * FROM notifications WHERE to_user_id=? ORDER BY data DESC LIMIT ? OFFSET ?";
        return fetchNotificationKeys(sql, user.getId(), limit, offset);
    }

    public Collection<Long> getUnreadNotificationKeysPage(User user, int offset, int limit) {
        if (user == null) throw new NotLoggedIn("User is not logged in");

        String sql = "SELECT * FROM notifications WHERE to_user_id=? AND read=false ORDER BY data DESC LIMIT ? OFFSET ?";
        return fetchNotificationKeys(sql, user.getId(), limit, offset);
    }

    public int pageCountAll(User user, int pageSize) {
        return (int) Math.ceil((double) getAllNotificationKeys(user).size() / pageSize);
    }

    public int pageCountUnread(User user, int pageSize) {
        return (int) Math.ceil((double) getUnreadNotificationKeys(user).size() / pageSize);
    }

    @Override
    public Collection<Notification> getAll() {
        throw new UnsupportedOperationException("You cannot access all requests in database.");
    }

    @Override
    public Collection<Long> getKeys() {
        throw new UnsupportedOperationException("You cannot access all requests in database.");
    }

    @Override
    public Collection<Notification> getPage(int offset, int limit) {
        throw new UnsupportedOperationException("You cannot access all requests in database.");
    }

    @Override
    public int pageCount(int pageSize) {
        throw new UnsupportedOperationException("You cannot access all requests in database.");
    }
}
/*
    CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    to_user_id BIGINT NOT NULL,
    data TIMESTAMP NOT NULL,
    text TEXT NOT NULL,
    read BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_to_user
        FOREIGN KEY(to_user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

*/
