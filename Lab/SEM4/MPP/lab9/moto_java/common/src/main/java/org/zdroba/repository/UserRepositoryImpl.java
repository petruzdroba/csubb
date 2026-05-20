package org.zdroba.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zdroba.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    private final Connection connection  = DataBaseConnection.getInstance().getConnection();
    private static final Logger logger = LogManager.getLogger();

    @Override
    public User find(Long id) {
        logger.traceEntry();
        String sql = "SELECT * FROM users WHERE id = ? LIMIT 1";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setLong(1, id);

            try(ResultSet rs = ps.executeQuery()){

                if(rs.next()){
                    String email = rs.getString("email");
                    String password = rs.getString("password");

                    User user = new User(email, password);
                    user.setId(id);

                    logger.traceExit();
                    return user;
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            System.err.println(e.getMessage());
        }

        logger.traceExit();
        return null;
    }

    @Override
    public User find(String email) {
        logger.traceEntry();
        String sql = "SELECT * FROM users WHERE email = ? LIMIT 1";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, email);

            try(ResultSet rs = ps.executeQuery()){

                if(rs.next()){
                    Long id = rs.getLong("id");
                    String password = rs.getString("password");

                    User user = new User(email, password);
                    user.setId(id);

                    logger.traceExit();
                    return user;
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            System.err.println(e.getMessage());
        }

        logger.traceExit();
        return null;
    }

    @Override
    public List<User> getAll() {
        logger.traceEntry();
        String sql = "SELECT * FROM users";

        List<User> users = new ArrayList<>();

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){

                while(rs.next()){
                    Long id = rs.getLong("id");
                    String email = rs.getString("email");
                    String password = rs.getString("password");

                    User user = new User(email, password);
                    user.setId(id);

                    users.add(user);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            System.err.println(e.getMessage());
        }

        logger.traceExit();
        return users;
    }

    @Override
    public void add(User user) {
        logger.traceEntry("Saving User{}", user);
        String sql = "INSERT INTO users (email, password) VALUES (?,?)";

        try(PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());

            int result = ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getLong(1));
            }

            logger.trace("Saved {} instance", result);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            System.err.println(e.getMessage());
        }

        logger.traceExit();
    }
}
