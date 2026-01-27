package org.example.repo;

import org.example.domain.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MeciRepo extends DatabaseConnection {
    private List<Observer> observer = new ArrayList<>();

    public MeciRepo(String url, String user, String password) {
        super(url, user, password);
    }

    public MeciRepo(DataBaseConfig config) {
        super(config);
    }

    public List<Meci> getAll() {
        String sql = "select * from matches";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {


            List<Meci> entities = new ArrayList<>();
            while (rs.next()) {
                Meci meci = new Meci(
                        rs.getLong("id"),
                        rs.getString("nume_gazda"),
                        rs.getString("nume_oaspete"),
                        rs.getInt("scor_gazda"),
                        rs.getInt("scor_oaspete")
                );

                entities.add(meci);
            }
            return entities;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Meci find(Long key) {
        String sql = "SELECT * FROM matches WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);) {

            ps.setLong(1, key);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                return new Meci(
                        rs.getLong("id"),
                        rs.getString("nume_gazda"),
                        rs.getString("nume_oaspete"),
                        rs.getInt("scor_gazda"),
                        rs.getInt("scor_oaspete")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void modify(Meci entity) throws SQLException {
        String sql = """
                    UPDATE matches
                    SET scor_oaspete = ?, scor_gazda = ?
                    WHERE id = ?
                """;


        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(3, entity.getId());
            ps.setInt(1, entity.getScorOaspete());
            ps.setInt(2, entity.getScorGazda());

            ps.executeUpdate();
        }
    }


}
