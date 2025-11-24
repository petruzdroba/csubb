package com.repo;

import com.domain.Card;
import com.domain.DataBaseConfig;
import com.domain.Duck;
import com.exceptions.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CardRepository extends AbstractDatabaseRepository<Duck.TipRata, Card> {

    public CardRepository(String url, String user, String password) {
        super(url, user, password);
    }

    public CardRepository(DataBaseConfig config) {
        super(config);
    }

    @Override
    public void add(Duck.TipRata key, Card entity) throws SQLException {
        String sql = "INSERT INTO cards (id, nume_card) VALUES (?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, entity.getId());
            ps.setString(2, entity.getNumeCard());
            ps.executeUpdate();
        }
    }

    public void addDuck(Duck.TipRata type, long duckId) throws SQLException {
        String sql = "INSERT INTO card_members (card_id, duck_id) VALUES (?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            Card card = find(type);
            if (card != null) {
                ps.setLong(1, card.getId());
                ps.setLong(2, duckId);
                ps.executeUpdate();

                if (type == Duck.TipRata.FLYING_AND_SWIMMING) {
                    ps.setLong(1, card.getId() - 1);
                    ps.setLong(2, duckId);
                    ps.executeUpdate();
                }
            }

        } catch (SQLException e) {
            throw new RepositoryException("Failed to add duck to card: " + e.getMessage());
        }
    }

    @Override
    public void remove(Duck.TipRata key) throws SQLException {
        Card card = find(key);
        if (card == null) return;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM cards WHERE id=?")) {
            ps.setLong(1, card.getId());
            ps.executeUpdate();
        }
    }

    public void removeDuck(Duck.TipRata type, long duckId) throws SQLException {
        Card card = find(type);
        if (card == null) throw new RepositoryException(type + " card does not exist");

        String sql = "DELETE FROM card_members WHERE card_id=? AND duck_id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, card.getId());
            ps.setLong(2, duckId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Failed to remove duck from card: " + e.getMessage());
        }
    }

    @Override
    public void modify(Duck.TipRata key, Card entity) throws SQLException {
        String sql = " UPDATE cards SET nume_card=? WHERE id=?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getNumeCard());
            ps.setLong(2, entity.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public Card find(Duck.TipRata key) throws SQLException {
        String sql = "SELECT * from cards WHERE id=?";
        long cardId = key == Duck.TipRata.SWIMMING ? 1 : 2;

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, cardId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                Card card = new Card(rs.getLong("id"), rs.getString("nume_card"));
                loadCardMembers(connection, card);
                return card;
            }
        }
    }

    @Override
    public Collection<Card> getAll() {
        String sql = "SELECT * FROM cards";

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<Card> cards = new ArrayList<>();

            while (rs.next()) {
                long cardId = rs.getLong("id");
                String name = rs.getString("nume_card");

                Card card = new Card(cardId, name);
                loadCardMembers(connection, card);
                cards.add(card);
            }

            return cards;
        } catch (SQLException e) {
            throw new RepositoryException("Failed to fetch values from DB: " + e.getMessage());
        }
    }

    private void loadCardMembers(Connection connection, Card card) throws SQLException {
        String sql = "SELECT duck_id FROM card_members WHERE card_id=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, card.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    card.addDuck(rs.getLong("duck_id"));
                }
            }
        }
    }

    @Override
    public Collection<Duck.TipRata> getKeys() {
        String sql = "SELECT DISTINCT type FROM cards";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<Duck.TipRata> types = new ArrayList<>();
            while (rs.next()) {
                types.add(Duck.TipRata.valueOf(rs.getString("type")));
            }
            return types;

        } catch (SQLException e) {
            throw new RepositoryException("Failed to fetch keys from DB: " + e.getMessage());
        }
    }

}
