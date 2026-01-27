package org.example.repo;

import org.example.domain.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EvenimentRepo extends DatabaseConnection{
    public EvenimentRepo(String url, String user, String password) {
        super(url, user, password);
    }

    public EvenimentRepo(DataBaseConfig config) {
        super(config);
    }

    public void add(Eveniment entity) throws SQLException {
        String sql = "INSERT INTO eveniment (match_id, team, rata, action) VALUES (?, ?, ?,?)";

        try(Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setLong(1,entity.getMatchId());
            ps.setString(2,entity.getTeam().toString());
            ps.setLong(3, entity.getRata());
            ps.setString(4, entity.getAction().toString());

            ps.executeUpdate();
        }
    }

    public List<Eveniment> getAll(Long key) {
        String sql = "select * from eveniment where match_id=?";

        try(Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);){

            ps.setLong(1, key);

            try(ResultSet rs = ps.executeQuery()){

                List<Eveniment> entities = new ArrayList<>();
                while (rs.next()) {
                    Eveniment entity = new Eveniment(
                            rs.getLong("id"),
                            key,
                            Team.valueOf(rs.getString("team")),
                            Math.toIntExact(rs.getLong("rata")),
                            Action.valueOf(rs.getString("action"))
                    );

                    entities.add(entity);
                }
                return entities;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
