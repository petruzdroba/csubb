package org.zdroba.repository;

import org.zdroba.entity.Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagRepository implements ITagRepository {

    private static TagRepository instance;

    private TagRepository() {}

    public static TagRepository getInstance() {
        if (instance == null)
            instance = new TagRepository();
        return instance;
    }

    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public void add(Tag entity) {
        String sql = "INSERT INTO tags (name) VALUES (?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getName());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void delete(Long key) {
        String sql = "DELETE FROM tags WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, key);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void update(Long key, Tag entity) {
        String sql = "UPDATE tags SET name = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ps.setLong(2, key);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public Tag find(Long key) {
        String sql = "SELECT * FROM tags WHERE id = ? LIMIT 1";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, key);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");

                    Tag tag = new Tag(name);
                    tag.setId(key);

                    return tag;
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    @Override
    public List<Tag> getAll() {
        String sql = "SELECT * FROM tags";
        List<Tag> tags = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Long id = rs.getLong("id");
                    String name = rs.getString("name");

                    Tag tag = new Tag(name);
                    tag.setId(id);

                    tags.add(tag);
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return tags;
    }

    @Override
    public List<Long> getKeys() {
        String sql = "SELECT id FROM tags";
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