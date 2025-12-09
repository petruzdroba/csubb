package com.repo;

import com.domain.CurrentUser;
import com.domain.DataBaseConfig;
import com.domain.Message;
import com.domain.User;

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

                insertMessage(connection, entity);
                insertReceivers(connection, key, entity.getTo());

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    private void insertMessage(Connection connection, Message message) throws SQLException{
        String sql = "INSERT INTO messages (id, from_user_id, message, data, reply_id) VALUES (?,?,?,?,?)";

        try(PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setLong(1,message.getId());
            ps.setLong(2, message.getFrom().getId());
            ps.setString(3,message.getMessage());
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(message.getData()));
            if (message.getReply() != null) {
                ps.setLong(5, message.getReply().getId());
            } else {
                ps.setNull(5, java.sql.Types.BIGINT);
            }
            ps.executeUpdate();
        }
    }

    private void insertReceivers(Connection connection,Long messageId, List<User> receivers) throws SQLException {
        String sql = "INSERT INTO message_to (message_id, user_id) VALUES (?, ?)";

        try(PreparedStatement ps= connection.prepareStatement(sql)){
            for(User u: receivers){
                ps.setLong(1, messageId);
                ps.setLong(2, u.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    @Override
    public void remove(Long key) throws SQLException {
        String sql = "DELETE FROM emessages WHERE id=?";

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
        String sql = "SELECT user_id FROM messages_to WHERE message_id=?";
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

    @Override
    public Collection<Message> getAll() {
        User current = CurrentUser.getInstance().getUser();
        String sql = "SELECT * FROM messages m " +
                "JOIN message_to mt ON m.id = mt.message_id " +
                "WHERE m.from_user_id = ? OR mt.user_id = ? " +
                "ORDER BY m.data";

        List<Message> messages = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, current.getId());
            ps.setLong(2, current.getId());

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

    @Override
    public Collection<Long> getKeys() {
        return List.of();
    }

    @Override
    public Collection<Message> getPage(int offset, int limit) {
        User current = CurrentUser.getInstance().getUser();

        String sql = "SELECT DISTINCT m.id FROM messages m " +
                "JOIN message_to mt ON m.id = mt.message_id " +
                "WHERE m.from_user_id = ? OR mt.user_id = ? " +
                "ORDER BY m.data " +
                "LIMIT ? OFFSET ?";

        List<Message> page = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, current.getId());
            ps.setLong(2, current.getId());
            ps.setInt(3, limit);
            ps.setInt(4, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    page.add(find(rs.getLong("id")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return page;
    }


    @Override
    public int pageCount(int pageSize) {
        Collection<Long> keys = getKeys();
        return (int) Math.ceil((double) keys.size() / pageSize);
    }

}
