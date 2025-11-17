package com.repo;

import com.domain.Card;
import com.domain.Duck;
import com.domain.User;
import com.exceptions.RepositoryException;

import java.nio.file.ReadOnlyFileSystemException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CardRepository extends AbstractDatabaseRepository<Duck.TipRata, Card> {

    public CardRepository(String url, String user, String password) {
        super(url, user, password);
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
        Card card = find(type);
        if (card == null)
            throw new RepositoryException(type + " card nu exista");

        String sql = "INSERT INTO card_members (card_id, duck_id) VALUES (?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, card.getId());
            ps.setLong(2, duckId);

            ps.executeQuery();
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

                // load members
                try (PreparedStatement ps2 = connection.prepareStatement("SELECT duck_id FROM card_members WHERE card_id=?")) {
                    ps2.setLong(1, card.getId());
                    try (ResultSet rs2 = ps2.executeQuery()) {
                        while (rs2.next()) {
                            card.addDuck(rs2.getLong("duck_id"));
                        }
                    }
                }
                return card;
            }
        }
    }

    @Override
    public Collection<Card> getAll() {
        String sqlCards = "SELECT * FROM cards";
        String sqlCardDucks = "SELECT duck_id FROM card_members WHERE card_id = ?";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rsCards = stmt.executeQuery(sqlCards)) {

            List<Card> cards = new ArrayList<>();
            while (rsCards.next()) {
                long cardId = rsCards.getLong("id");
                String name = rsCards.getString("nume_card");

                Card card = new Card(cardId, name);

                try (PreparedStatement ps = conn.prepareStatement(sqlCardDucks)) {
                    ps.setLong(1, cardId);
                    try (ResultSet rsDucks = ps.executeQuery()) {
                        List<Long> duckIds = new ArrayList<>();
                        while (rsDucks.next()) {
                            duckIds.add(rsDucks.getLong("duck_id"));
                        }
                        card.setDuckIds(duckIds);
                    }
                }
                cards.add(card);
            }
            return cards;
        } catch (SQLException e) {
            throw new RepositoryException("Failed to fetch values from DB: " + e.getMessage());
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
