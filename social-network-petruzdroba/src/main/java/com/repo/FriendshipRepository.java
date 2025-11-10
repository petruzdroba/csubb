package com.repo;

import com.domain.Friendship;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class FriendshipRepository extends AbstractDatabaseRepository<String, Friendship> {

    public FriendshipRepository(String url, String user, String password) {
        super(url, user, password);
    }

    @Override
    protected void loadFromDb() {
        String sql = "SELECT * FROM friendships";

        try(Connection connection = getConnection();
        var stmt = connection.createStatement();
        var rs = stmt.executeQuery(sql)){

            while(rs.next()){
                String id = rs.getString("friendship_id");
                long userId1 = rs.getLong("user_id1");
                long userId2 = rs.getLong("user_id2");

                Friendship f = new Friendship(userId1, userId2);
                data.put(id, f);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void addToDb(String key, Friendship entity) throws SQLException {
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
    protected void removeFromDb(String key) throws SQLException {
        String sql = "DELETE FROM friendships WHERE friendship_id = ?";
        try(Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, key);
            ps.executeUpdate();
        }
    }

    @Override
    protected void modifyInDb(String key, Friendship entity) throws SQLException {
        throw new UnsupportedOperationException("Friendships cannot be modified, only added or removed.");
    }

    /**
     *Gaseste toate prieteniile care il au pe user {@link com.domain.User} cu userId
     *  si sterge prieteniile care il contin
     *
     * @param userId, id-ul userului care a fost sters, cascade delete freindships
     * @see com.repo.AbstractRepository#remove(Object)
     * */
    public void removeUserFriendships(long userId){
        getAll().forEach(f -> {
            if(f.getUserId1() == userId || f.getUserId2() == userId)
                remove(f.getFriendshipId());
        });
    }
}
