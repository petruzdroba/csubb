package com.repo;

import com.domain.DataBaseConfig;
import com.domain.Message;
import com.domain.Request;
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

public class RequestRepository extends AbstractDatabaseRepository<Long, Request> {
    private final UserRepository userRepo;

    public RequestRepository(String url, String user, String password, UserRepository userRepo) {
        super(url, user, password);
        this.userRepo = userRepo;
    }

    public RequestRepository(DataBaseConfig config, UserRepository userRepo) {
        super(config);
        this.userRepo = userRepo;
    }

    @Override
    public void add(Long key, Request entity) throws SQLException {
        String sql = "INSERT INTO friend_requests (from_user_id, to_user_id, status, request_date) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, entity.getFrom().getId());
            ps.setLong(2, entity.getTo().getId());
            ps.setString(3, entity.getStatus().toString());
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(entity.getData()));

            ps.executeUpdate();
        }
    }

    @Override
    public void remove(Long key) throws SQLException {
        String sql = "DELETE FROM friend_requests WHERE id=?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, key);
            ps.executeUpdate();
        }
    }

    @Override
    public void modify(Long key, Request entity) throws SQLException {
        String sql = "UPDATE friend_requests SET status=? WHERE id=?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getStatus().toString());
            ps.setLong(2, key);

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new SQLException("No request found with id " + key);
            }
        }
    }

    public boolean exists(Long userId1, Long userId2) throws SQLException{
        String sql = "SELECT * FROM friend_requests WHERE from_user_id=? AND to_user_id=? AND status=?";

        try (Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setLong(1, userId1);
            ps.setLong(2, userId2);
            ps.setString(3, "PENDING");

            try(ResultSet rs = ps.executeQuery()){
                if(!rs.next()){
                    return false;
                }
                return true;
            }
        }
    }

    @Override
    public Request find(Long key) throws SQLException {
        String sql = "SELECT * FROM friend_requests WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, key);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                long id = rs.getLong("id");
                long fromUserId = rs.getLong("from_user_id");
                long toUserId = rs.getLong("to_user_id");
                String statusStr = rs.getString("status");
                LocalDateTime requestDate = rs.getTimestamp("request_date").toLocalDateTime();

                User fromUser = userRepo.find(fromUserId);
                if (fromUser == null) {
                    throw new SQLException("Sender not found for request " + id);
                }

                User toUser = userRepo.find(toUserId);
                if (toUser == null) {
                    throw new SQLException("Receiver not found for request " + id);
                }

                Request request = new Request();
                request.setId(id);
                request.setFrom(fromUser);
                request.setTo(toUser);
                request.setStatus(Request.status.valueOf(statusStr));
                request.setData(requestDate);

                return request;
            }
        }
    }

    private List<Request> fetchRequests(String sql, long userId, Integer limit, Integer offset) {
        List<Request> requests = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, userId);
            if (limit != null) ps.setInt(2, limit);
            if (offset != null) ps.setInt(3, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    requests.add(find(rs.getLong("id")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return requests;
    }

    public Collection<Request> getReceivedPage(User user, int offset, int limit) {
        if (user == null)
            throw new NotLoggedIn("User is not logged in");

        String sql = "SELECT * FROM friend_requests WHERE to_user_id=? AND status='PENDING' ORDER BY request_date DESC LIMIT ? OFFSET ?";

        return fetchRequests(sql, user.getId(), limit, offset);
    }

    public Collection<Request> getSentPage(User user, int offset, int limit) {
        if (user == null)
            throw new NotLoggedIn("User is not logged in");

        String sql = "SELECT * FROM friend_requests WHERE from_user_id=? AND status='PENDING' ORDER BY request_date DESC LIMIT ? OFFSET ?";

        return fetchRequests(sql, user.getId(), limit, offset);
    }

    public Collection<Request> getReceived(User user) {
        if (user == null)
            throw new NotLoggedIn("User is not logged in");

        String sql = "SELECT * FROM friend_requests WHERE to_user_id=? AND status='PENDING' ORDER BY request_date DESC";

        return fetchRequests(sql, user.getId(), null, null);
    }

    public Collection<Request> getSent(User user) {
        if (user == null)
            throw new NotLoggedIn("User is not logged in");

        String sql = "SELECT * FROM friend_requests WHERE from_user_id=? AND status='PENDING' ORDER BY request_date DESC";

        return fetchRequests(sql, user.getId(), null, null);
    }

    private List<Long> fetchKeys(String sql, long userId) {
        List<Long> keys = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, userId);

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

    private List<Long> fetchRequestKeys(String sql, long userId, Integer limit, Integer offset) {
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

    public Collection<Long> getReceivedKeysPage(User user, int offset, int limit) {
        if (user == null) throw new NotLoggedIn("User is not logged in");

        String sql = "SELECT * FROM friend_requests WHERE to_user_id=? AND status='PENDING' ORDER BY request_date DESC LIMIT ? OFFSET ?";
        return fetchRequestKeys(sql, user.getId(), limit, offset);
    }

    public Collection<Long> getSentKeysPage(User user, int offset, int limit) {
        if (user == null) throw new NotLoggedIn("User is not logged in");

        String sql = "SELECT * FROM friend_requests WHERE from_user_id=? AND status='PENDING' ORDER BY request_date DESC LIMIT ? OFFSET ?";
        return fetchRequestKeys(sql, user.getId(), limit, offset);
    }

    public Collection<Long> getReceivedKeys(User user) {
        if (user == null) throw new NotLoggedIn("User is not logged in");

        String sql = "SELECT * FROM friend_requests WHERE to_user_id=? AND status='PENDING' ORDER BY request_date DESC";
        return fetchRequestKeys(sql, user.getId(), null, null);
    }

    public Collection<Long> getSentKeys(User user) {
        if (user == null) throw new NotLoggedIn("User is not logged in");

        String sql = "SELECT * FROM friend_requests WHERE from_user_id=? AND status='PENDING' ORDER BY request_date DESC";
        return fetchRequestKeys(sql, user.getId(), null, null);
    }

    public int pageCountSent(User user, int pageSize) {
        return (int) Math.ceil((double) getSentKeys(user).size() / pageSize);
    }

    public int pageCountReceived(User user, int pageSize) {
        return (int) Math.ceil((double) getReceivedKeys(user).size() / pageSize);
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
    public Collection<Request> getAll() {
        throw new UnsupportedOperationException("You cannot access all requests in database.");
    }

    @Override
    public Collection<Long> getKeys() {
        throw new UnsupportedOperationException("You cannot access all requests in database.");
    }

    @Override
    public Collection<Request> getPage(int offset, int limit) {
        throw new UnsupportedOperationException("You cannot access all requests in database.");
    }

    @Override
    public int pageCount(int pageSize) {
        throw new UnsupportedOperationException("You cannot access all requests in database.");
    }
}

/*
* CREATE TABLE friend_requests (
    id BIGSERIAL PRIMARY KEY,
    from_user_id BIGINT NOT NULL,
    to_user_id BIGINT NOT NULL,
    status VARCHAR(10) NOT NULL DEFAULT 'PENDING',
    request_date TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_from_user
        FOREIGN KEY (from_user_id) REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_to_user
        FOREIGN KEY (to_user_id) REFERENCES users(id)
        ON DELETE CASCADE
);
* */
