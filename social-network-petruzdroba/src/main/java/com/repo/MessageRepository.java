package com.repo;

import com.domain.DataBaseConfig;
import com.domain.Message;
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

public class MessageRepository extends AbstractDatabaseRepository<Long, Message>{
    private final UserRepository userRepo;

    public MessageRepository(String url, String user, String password, UserRepository userRepo) {
        super(url, user, password);
        this.userRepo = userRepo;
    }

    public MessageRepository(DataBaseConfig config, UserRepository userRepo) {
        super(config);
        this.userRepo = userRepo;
    }

    @Override
    public void add(Long key, Message entity) throws SQLException {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try {
                long messageId = insertMessage(connection, entity);
                insertReceivers(connection, messageId, entity.getTo());

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }


    private long insertMessage(Connection connection, Message message) throws SQLException {
        String sql = "INSERT INTO messages (from_user_id, message, data, reply_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, message.getFrom().getId());
            ps.setString(2, message.getMessage());
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(message.getData()));
            if (message.getReply() != null) {
                ps.setLong(4, message.getReply().getId());
            } else {
                ps.setNull(4, java.sql.Types.BIGINT);
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);//db genearted id
                } else {
                    throw new SQLException("Failed to get generated ID for message");
                }
            }
        }
    }


    private void insertReceivers(Connection connection, long messageId, List<User> receivers) throws SQLException {
        String sql = "INSERT INTO message_to (message_id, user_id) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (User u : receivers) {
                ps.setLong(1, messageId);
                ps.setLong(2, u.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    @Override
    public void remove(Long key) throws SQLException {
        String sql = "DELETE FROM messages WHERE id=?";

        try(Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setLong(1, key);
            ps.executeUpdate();
        }
    }

    @Override
    public void modify(Long key, Message entity) throws SQLException {
        throw new UnsupportedOperationException("Messages cannot be modified once sent.");
    }

    @Override
    public Message find(Long key) throws SQLException {
        String sql = "SELECT * FROM messages WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, key);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                long id = rs.getLong("id");
                long fromUserId = rs.getLong("from_user_id");
                String messageText = rs.getString("message");
                LocalDateTime date = rs.getTimestamp("data").toLocalDateTime();
                Long replyId = rs.getObject("reply_id", Long.class);//null posibil

                User fromUser = userRepo.find(fromUserId);
                if (fromUser == null) {
                    throw new SQLException("Sender not found for message " + id);
                }

                List<User> receivers = fetchReceivers(connection, id);

                Message replyMessage = null;
                if (replyId != null) {
                    replyMessage = find(replyId);
                }

                return new Message(id, fromUser, messageText, date, replyMessage, receivers);
            }
        }
    }

    private List<User> fetchReceivers(Connection connection, Long messageId) throws SQLException {
        String sql = "SELECT user_id FROM message_to WHERE message_id=?";
        List<User> receivers = new ArrayList<>();

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setLong(1, messageId);

            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    long userId = rs.getLong("user_id");
                    User user = userRepo.find(userId);
                    if(user != null)
                        receivers.add(user);
                }
            }
        }
        return receivers;
    }

    private List<Message> fetchMessages(String sql, long userId, Integer limit, Integer offset) {
        List<Message> messages = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, userId);
            if (limit != null) ps.setInt(2, limit);
            if (offset != null) ps.setInt(3, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    messages.add(find(rs.getLong("id")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return messages;
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


    public Collection<Message> getReceived(User user) {
        if(user == null)
            throw new NotLoggedIn("User is not logged in");

        String sql = "SELECT DISTINCT m.id, m.data FROM messages m " +
                "JOIN message_to mt ON m.id = mt.message_id " +
                "WHERE mt.user_id = ? " +
                "ORDER BY m.data DESC";

        return fetchMessages(sql, user.getId(), null, null);
    }

    public Collection<Long> getReceivedKeys(User user) {
        if(user == null)
            throw new NotLoggedIn("User is not logged in");

        String sql = "SELECT DISTINCT m.id FROM messages m " +
                "JOIN message_to mt ON m.id = mt.message_id " +
                "WHERE mt.user_id = ?";

        return fetchKeys(sql, user.getId());
    }


    public Collection<Message> getReceivedPage(User user, int offset, int limit) {
        if(user == null)
            throw new NotLoggedIn("User is not logged in");

        String sql = "SELECT DISTINCT m.id, m.data FROM messages m " +
                "JOIN message_to mt ON m.id = mt.message_id " +
                "WHERE mt.user_id = ? " +
                "ORDER BY m.data DESC " +
                "LIMIT ? OFFSET ?";

        return fetchMessages(sql, user.getId(), limit, offset);
    }


    public int pageCountReceived(User user, int pageSize) {
        return (int) Math.ceil((double) getReceivedKeys(user).size() / pageSize);
    }

    public Collection<Message> getSent(User user) {
        if(user == null)
            throw new NotLoggedIn("User is not logged in");

        String sql = "SELECT m.id, m.data FROM messages m " +
                "WHERE m.from_user_id = ? " +
                "ORDER BY m.data DESC";

        return fetchMessages(sql, user.getId(), null, null);
    }

    public Collection<Long> getSentKeys(User user) {
        if(user == null)
            throw new NotLoggedIn("User is not logged in");

        String sql = "SELECT m.id FROM messages m " +
                "WHERE m.from_user_id = ?";

        return fetchKeys(sql, user.getId());
    }

    public Collection<Message> getSentPage(User user, int offset, int limit) {
        if(user == null)
            throw new NotLoggedIn("User is not logged in");

        String sql = "SELECT m.id, m.data FROM messages m " +
                "WHERE m.from_user_id = ? " +
                "ORDER BY m.data DESC " +
                "LIMIT ? OFFSET ?";

        return fetchMessages(sql, user.getId(), limit, offset);
    }

    public int pageCountSent(User user, int pageSize) {
        return (int) Math.ceil((double) getSentKeys(user).size() / pageSize);
    }

    @Override
    public Collection<Message> getAll() {
        throw new UnsupportedOperationException("You cannot access all messages in database.");
    }

    @Override
    public Collection<Long> getKeys() {
        throw new UnsupportedOperationException("You cannot access all messages in database.");
    }

    @Override
    public Collection<Message> getPage(int offset, int limit) {
        throw new UnsupportedOperationException("You cannot access all messages in database.");
    }

    @Override
    public int pageCount(int pageSize) {
        throw new UnsupportedOperationException("You cannot access all messages in database.");
    }
}

/*
* CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    from_user_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    data TIMESTAMP NOT NULL,
    reply_id BIGINT,
    FOREIGN KEY (from_user_id) REFERENCES users(id),
    FOREIGN KEY (reply_id) REFERENCES messages(id)
);
CREATE TABLE message_to (
    message_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (message_id, user_id),
    FOREIGN KEY (message_id) REFERENCES messages(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
* */
