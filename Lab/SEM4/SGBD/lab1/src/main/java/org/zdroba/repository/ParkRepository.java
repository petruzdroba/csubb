package org.zdroba.repository;

import org.zdroba.entity.Park;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParkRepository implements IParkRepository {

    private static ParkRepository instance;

    private ParkRepository() {}

    public static ParkRepository getInstance() {
        if (instance == null)
            instance = new ParkRepository();
        return instance;
    }

    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public void add(Park entity) {
        String sql = "INSERT INTO parks (name, country) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getCounty());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void delete(Long key) {
        String sql = "DELETE FROM parks WHERE id=?";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setLong(1, key);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void update(Long key, Park entity) {
        String sql = "UPDATE parks SET name=?, country=? WHERE id=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getCounty());
            ps.setLong(3, key);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public Park find(Long key) {
        String sql = "SELECT * FROM parks WHERE id=?";

        try(PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setLong(1,key);
            try(ResultSet rs = ps.executeQuery()){

                if(rs.next()){
                    String name = rs.getString("name");
                    String country = rs.getString("country");

                    Park park = new Park(name, country);
                    park.setId(key);

                    return park;
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    @Override
    public List<Park> getAll() {
        String sql = "SELECT * FROM parks";
        List<Park> parks = new ArrayList<>();

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    Long id = rs.getLong("id");
                    String name = rs.getString("name");
                    String country = rs.getString("country");

                    Park park = new Park(name, country);
                    park.setId(id);

                    parks.add(park);
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return parks;
    }

    @Override
    public List<Long> getKeys() {
        String sql = "SELECT id FROM parks";
        List<Long> ids = new ArrayList<>();

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    Long id = rs.getLong("id");

                    ids.add(id);
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return ids;
    }
}
