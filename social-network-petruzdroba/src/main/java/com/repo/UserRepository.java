package com.repo;
import com.domain.*;
import com.exceptions.RepositoryException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserRepository extends AbstractDatabaseRepository<Long, User>{

    public UserRepository(String url, String user, String password) {
        super(url, user, password);
    }

    public UserRepository(DataBaseConfig config) {
        super(config);
    }

    @Override
    public void add(Long key, User entity) throws SQLException {
        String sql = "INSERT INTO users (id, username, email, password, user_type, tip_rata, viteza, rezistenta, nume, prenume, data_nasterii, ocupatie, nivel_empatie)\n"+
                " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setLong(1, entity.getId());
            ps.setString(2, entity.getUsername());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getPassword());

            if (entity instanceof Duck duck) {
                ps.setString(5, "DUCK");
                ps.setString(6, duck.getTip().name());
                ps.setDouble(7, duck.getViteza());
                ps.setDouble(8, duck.getRezistenta());
                ps.setNull(9, Types.VARCHAR);
                ps.setNull(10, Types.VARCHAR);
                ps.setNull(11, Types.DATE);
                ps.setNull(12, Types.VARCHAR);
                ps.setNull(13, Types.INTEGER);
            } else if (entity instanceof Persoana p) {
                ps.setString(5, "PERSONA");
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.DOUBLE);
                ps.setNull(8, Types.DOUBLE);
                ps.setString(9, p.getNume());
                ps.setString(10, p.getPrenume());
                ps.setDate(11, Date.valueOf(p.getDataNasterii()));
                ps.setString(12, p.getOcupatie());
                ps.setInt(13, p.getNivelEmpatie());
            }
            ps.executeUpdate();
        }
    }

    @Override
    public void remove(Long key) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try(Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setLong(1, key);
            ps.executeUpdate();
        }
    }

    @Override
    public void modify(Long key, User entity) throws SQLException {
        String sql = """
        UPDATE users SET 
            username = ?, email = ?, password = ?, user_type = ?, 
            tip_rata = ?, viteza = ?, rezistenta = ?,
            nume = ?, prenume = ?, data_nasterii = ?, ocupatie = ?, nivel_empatie = ?
        WHERE id = ?
    """;

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getEmail());
            ps.setString(3, entity.getPassword());

            if (entity instanceof Duck duck) {
                ps.setString(4, "DUCK");
                ps.setString(5, duck.getTip().name());
                ps.setDouble(6, duck.getViteza());
                ps.setDouble(7, duck.getRezistenta());
                ps.setNull(8, Types.VARCHAR);
                ps.setNull(9, Types.VARCHAR);
                ps.setNull(10, Types.DATE);
                ps.setNull(11, Types.VARCHAR);
                ps.setNull(12, Types.INTEGER);
            } else if (entity instanceof Persoana p) {
                ps.setString(4, "PERSONA");
                ps.setNull(5, Types.VARCHAR);
                ps.setNull(6, Types.DOUBLE);
                ps.setNull(7, Types.DOUBLE);
                ps.setString(8, p.getNume());
                ps.setString(9, p.getPrenume());
                ps.setDate(10, Date.valueOf(p.getDataNasterii()));
                ps.setString(11, p.getOcupatie());
                ps.setInt(12, p.getNivelEmpatie());
            }

            ps.setLong(13, key);
            ps.executeUpdate();
        }
    }

    @Override
    public User find(Long key) throws SQLException {
        String sql = "SELECT * FROM users WHERE id=?";

        try(Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setLong(1, key);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return null;
                }

                long id = rs.getLong("id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String userType = rs.getString("user_type");

                if ("DUCK".equalsIgnoreCase(userType)) {
                    Duck.TipRata tip = Duck.TipRata.valueOf(rs.getString("tip_rata"));
                    double viteza = rs.getDouble("viteza");
                    double rezistenta = rs.getDouble("rezistenta");

                    return switch (tip) {
                        case FLYING -> new FlyingDuck(id, username, email, password, tip, viteza, rezistenta);
                        case SWIMMING -> new SwimmingDuck(id, username, email, password, tip, viteza, rezistenta);
                        default -> new SwimmingFlyingDuck(id, username, email, password, tip, viteza, rezistenta);
                    };
                }

                // Persona
                String nume = rs.getString("nume");
                String prenume = rs.getString("prenume");
                LocalDate dataNasterii = rs.getDate("data_nasterii").toLocalDate();
                String ocupatie = rs.getString("ocupatie");
                int nivelEmpatie = rs.getInt("nivel_empatie");

                return new Persoana(id, username, email, password, nume, prenume, dataNasterii, ocupatie, nivelEmpatie);
            }
        }
    }

    private User mapResultSetToEntity(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String username = rs.getString("username");
        String email = rs.getString("email");
        String password = rs.getString("password");
        String userType = rs.getString("user_type");

        if ("DUCK".equalsIgnoreCase(userType)) {
            Duck.TipRata tip = Duck.TipRata.valueOf(rs.getString("tip_rata"));
            double viteza = rs.getDouble("viteza");
            double rezistenta = rs.getDouble("rezistenta");

            if (tip == Duck.TipRata.FLYING) {
                return  new FlyingDuck(id, username, email, password, tip, viteza, rezistenta);
            } else if (tip == Duck.TipRata.SWIMMING) {
                return new SwimmingDuck(id, username, email, password, tip, viteza, rezistenta);
            } else {
                return  new SwimmingFlyingDuck(id, username, email, password, tip, viteza, rezistenta);
            }

        } else { // PERSONA
            String nume = rs.getString("nume");
            String prenume = rs.getString("prenume");
            LocalDate dataNasterii = rs.getDate("data_nasterii").toLocalDate();
            String ocupatie = rs.getString("ocupatie");
            int nivelEmpatie = rs.getInt("nivel_empatie");

            return  new Persoana(id, username, email, password, nume, prenume, dataNasterii, ocupatie, nivelEmpatie);
        }
    }


    @Override
    public Collection<User> getAll() {
        String sql = "SELECT * FROM users";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<User> values = new ArrayList<>();
            while (rs.next()) {
                User entity = mapResultSetToEntity(rs);
                values.add(entity);
            }
            return values;

        } catch (SQLException e) {
            throw new RepositoryException("Failed to fetch values from DB: " + e.getMessage());
        }
    }

    @Override
    public Collection<Long> getKeys() {
        String sql = "SELECT id FROM users";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<Long> keys = new ArrayList<>();
            while (rs.next()) {
                keys.add(rs.getLong("id"));
            }
            return keys;

        } catch (SQLException e) {
            throw new RepositoryException("Failed to fetch keys from DB: " + e.getMessage());
        }
    }
}