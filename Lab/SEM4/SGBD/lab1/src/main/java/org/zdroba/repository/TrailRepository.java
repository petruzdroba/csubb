package org.zdroba.repository;

import org.zdroba.entity.Park;
import org.zdroba.entity.Tag;
import org.zdroba.entity.Trail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrailRepository implements ITrailRepository {

    private static TrailRepository instance;

    private TrailRepository() {}

    public static TrailRepository getInstance() {
        if (instance == null)
            instance = new TrailRepository();
        return instance;
    }

    private final Connection connection = DatabaseConnection.getInstance().getConnection();
    private final ITagRepository tagRepository = TagRepository.getInstance();
    private final IParkRepository parkRepository = ParkRepository.getInstance();

    @Override
    public void add(Trail entity) {
        try {
            connection.setAutoCommit(false);
            Long key = insertTrail(entity);

            if (key == null) {
                connection.rollback();
                throw new SQLException("Insert failed");
            }

            insertTags(key, entity.getTags());
            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
            System.err.println(e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private Long insertTrail(Trail trail) throws SQLException {
        String sql = "INSERT INTO trails (name, length, park_id) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, trail.getName());
            ps.setDouble(2, trail.getLength());
            ps.setLong(3, trail.getPark().getId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        return null;
    }

    private void insertTags(Long key, List<Tag> tags) throws SQLException {
        String sql = "INSERT INTO trail_tags (trail_id, tag_id) VALUES (?, ?)";

        for(Tag tag: tags){
            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1,key);
                ps.setLong(2,tag.getId());

                ps.executeUpdate();
            }
        }
    }

    @Override
    public void delete(Long key) {
        String sql = "DELETE FROM trails WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, key);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void update(Long key,Trail entity) {
        try {
            connection.setAutoCommit(false);

            updateTrail(entity);
            deleteTrailTags(key);
            insertTags(key, entity.getTags());

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
            System.err.println(e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void updateTrail(Trail trail) throws SQLException {
        String sql = "UPDATE trails SET name = ?, length = ?, park_id = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, trail.getName());
            ps.setDouble(2, trail.getLength());
            ps.setLong(3, trail.getPark().getId());
            ps.setLong(4, trail.getId());
            ps.executeUpdate();
        }
    }

    private void deleteTrailTags(Long key) throws SQLException {
        String sql = "DELETE FROM trail_tags WHERE trail_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, key);
            ps.executeUpdate();
        }
    }

    @Override
    public Trail find(Long key) {
        String sql = "SELECT * FROM trails WHERE id=?";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setLong(1,key);

            try(ResultSet rs = ps.executeQuery()){

                if(rs.next()){
                    return mapToTrail(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    private Trail mapToTrail(ResultSet rs) throws SQLException {
        Long key = rs.getLong("id");
        String name = rs.getString("name");
        double length = rs.getDouble("length");
        Park park = parkRepository.find(rs.getLong("park_id"));
        List<Tag> tags = findTags(key);

        Trail trail = new Trail(name,length,park, tags);
        trail.setId(key);

        return trail;
    }

    private List<Tag> findTags(Long key) throws SQLException {
        String sql = "SELECT tag_id FROM trail_tags WHERE trail_id=?";
        List<Tag> tags = new ArrayList<>();

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setLong(1,key);

            try(ResultSet rs = ps.executeQuery()){

                while(rs.next()){
                    Tag tag = tagRepository.find(rs.getLong("tag_id"));

                    tags.add(tag);
                }
            }
        }

        return tags;
    }

    @Override
    public List<Trail> getAll() {
        String sql = "SELECT * FROM trails";
        List<Trail> trails = new ArrayList<>();

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){

                while(rs.next()){
                    trails.add(mapToTrail(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return trails;
    }

    @Override
    public List<Long> getKeys() {
        String sql = "SELECT id FROM trails";
        List<Long> ids = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getLong("id"));
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return ids;
    }
}
