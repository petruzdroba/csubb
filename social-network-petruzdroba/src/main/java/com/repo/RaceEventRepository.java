package com.repo;

import com.containers.DuckRaceContainer;
import com.domain.*;
import com.exceptions.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RaceEventRepository extends AbstractDatabaseRepository<Long, RaceEvent>{
    private final UserRepository userRepo;


    public RaceEventRepository(String url, String user, String password, UserRepository userRepo) {
        super(url, user, password);
        this.userRepo = userRepo;
    }

    public RaceEventRepository(DataBaseConfig config, UserRepository userRepo) {
        super(config);
        this.userRepo = userRepo;
    }

    @Override
    public void add(Long key, RaceEvent event) throws SQLException {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try {
                insertEvent(conn, event);
                insertDucks(conn, event);
                insertLanes(conn, event);
                insertSubscribers(conn, event);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    private void insertEvent(Connection conn, RaceEvent event) throws SQLException {
        String sql = "INSERT INTO events (owner_id) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, event.getOwnerId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    event.setId(rs.getLong(1));
                }
            }
        }
    }

    private void insertDucks(Connection conn, RaceEvent event) throws SQLException {
        String sql = "INSERT INTO event_ducks (event_id, duck_id) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Duck d : event.getContainer().getDucks()) {
                ps.setLong(1, event.getId());
                ps.setLong(2, d.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertLanes(Connection conn, RaceEvent event) throws SQLException {
        String sql = "INSERT INTO event_lanes (event_id, distance, lane_index) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Culoar c : event.getContainer().getCuloare()) {
                ps.setLong(1, event.getId());
                ps.setInt(2, c.getDistanta());
                ps.setInt(3, c.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertSubscribers(Connection conn, RaceEvent event) throws SQLException {
        String sql = "INSERT INTO event_subscribers (event_id, user_id) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (User u : event.getSubscribers()) {
                ps.setLong(1, event.getId());
                ps.setLong(2, u.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    @Override
    public void remove(Long id) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps =
                     conn.prepareStatement("DELETE FROM events WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public void modify(Long key, RaceEvent entity) throws SQLException {
        throw new UnsupportedOperationException("Modifying is not in the original task");
    }

    @Override
    public RaceEvent find(Long id) throws SQLException {
        String sql = "SELECT owner_id FROM events WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                long ownerId = rs.getLong("owner_id");

                List<Duck> ducks = fetchDucks(conn, id);
                List<Culoar> lanes = fetchLanes(conn, id);
                List<User> subs = fetchSubscribers(conn, id);

                DuckRaceContainer container = new DuckRaceContainer(ducks, lanes);
                RaceEvent event = new RaceEvent(id, ownerId, container);
                event.setSubscribers(subs);

                return event;
            }
        }
    }

    private List<Duck> fetchDucks(Connection conn, long eventId) throws SQLException {
        List<Duck> ducks = new ArrayList<>();
        String sql = "SELECT duck_id FROM event_ducks WHERE event_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Duck d = (Duck) userRepo.find(rs.getLong("duck_id"));
                    if (d != null) ducks.add(d);
                }
            }
        }
        return ducks;
    }

    private List<Culoar> fetchLanes(Connection conn, long eventId) throws SQLException {
        List<Culoar> lanes = new ArrayList<>();
        String sql = "SELECT distance, lane_index FROM event_lanes WHERE event_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lanes.add(new Culoar(
                            rs.getInt("distance"),
                            rs.getInt("lane_index")
                    ));
                }
            }
        }
        return lanes;
    }

    private List<User> fetchSubscribers(Connection conn, long eventId) throws SQLException {
        List<User> subs = new ArrayList<>();
        String sql = "SELECT user_id FROM event_subscribers WHERE event_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User u = userRepo.find(rs.getLong("user_id"));
                    if (u != null) subs.add(u);
                }
            }
        }
        return subs;
    }

    public Collection<RaceEvent> getAll() {
        String sql = "SELECT id FROM events";
        List<RaceEvent> events = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                long id = rs.getLong("id");
                RaceEvent e = find(id);
                if (e != null) events.add(e);
            }

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }

        return events;
    }


    public Collection<Long> getKeys() {
        String sql = "SELECT id FROM events";
        List<Long> keys = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                keys.add(rs.getLong("id"));
            }

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }

        return keys;
    }


    public Collection<RaceEvent> getPage(int offset, int limit) {
        String sql = "SELECT id FROM events ORDER BY id LIMIT ? OFFSET ?";
        List<RaceEvent> events = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RaceEvent e = find(rs.getLong("id"));
                    if (e != null) events.add(e);
                }
            }

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }

        return events;
    }

    @Override
    public int pageCount(int pageSize) {
        String sql = "SELECT COUNT(*) AS total FROM events";

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                int totalRows = rs.getInt("total");
                return (int) Math.ceil((double) totalRows / pageSize);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get page count: " + e.getMessage());
        }

        return 0;
    }

    public void subscribe(long eventId, User user) {
        String sql = "INSERT INTO event_subscribers (event_id, user_id) VALUES (?, ?)";

        try(Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setLong(1, eventId);
            ps.setLong(2, user.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    public void unsubscribe(long eventId, User user) {
        String sql = "DELETE FROM event_subscribers WHERE event_id=? AND user_id=?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, eventId);
            ps.setLong(2, user.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }
}
/*
CREATE TABLE events (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    owner_id BIGINT NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE event_ducks (
    event_id BIGINT NOT NULL,
    duck_id BIGINT NOT NULL,
    PRIMARY KEY (event_id, duck_id),
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    FOREIGN KEY (duck_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE event_lanes (
    event_id BIGINT NOT NULL,
    lane_index BIGINT NOT NULL,
    distance INT NOT NULL,
    PRIMARY KEY (event_id, lane_index),
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);

CREATE TABLE event_subscribers (
    event_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (event_id, user_id),
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

* */
