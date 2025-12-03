package com.repo;

import com.containers.DuckRaceContainer;
import com.domain.*;
import com.exceptions.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class EventRepository extends AbstractDatabaseRepository<Long, Event> {
    private final UserRepository userRepo;

    public EventRepository(String url, String user, String password, UserRepository userRepo) {
        super(url, user, password);
        this.userRepo = userRepo;
    }

    public EventRepository(DataBaseConfig config, UserRepository userRepo) {
        super(config);
        this.userRepo = userRepo;
    }

    @Override
    public void add(Long key, Event event) throws SQLException {
        if (!(event instanceof RaceEvent re)) throw new IllegalArgumentException("Only RaceEvent supported");

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try {
                insertEventId(connection, event.getId());
                insertDucks(connection, event.getId(), re.getContainer().getDucks());
                insertLanes(connection, event.getId(), re.getContainer().getCuloare());
                insertSubscribers(connection, event.getId(), re.getSubscribers());

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    private void insertEventId(Connection connection, Long eventId) throws SQLException {
        String sql = "INSERT INTO events (id) VALUES (?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, eventId);
            ps.executeUpdate();
        }
    }

    private void insertDucks(Connection connection, long eventId, Collection<Duck> ducks) throws SQLException {
        String sql = "INSERT INTO event_ducks (event_id, duck_id) VALUES (?, ?)";
        // event_id foreing key -> to event
        // duck_id foreign key -> to duck Cascade both, users table

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            for (Duck d : ducks) {
                ps.setLong(1, eventId);
                ps.setLong(2, d.getId());
                ps.addBatch(); // pregatim mai multe instructiuni, queue
            }
            ps.executeBatch(); // executam tot deoatata

        }
    }

    private void insertLanes(Connection connection, long eventId, Collection<Culoar> lanes) throws SQLException {
        String sql = "INSERT INTO event_lanes(event_id, distance, lane_index) VALUES(?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            for (Culoar c : lanes) {
                ps.setLong(1, eventId);
                ps.setInt(2, c.getDistanta());
                ps.setLong(3, c.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertSubscribers(Connection connection, long eventId, Collection<User> subscribers) throws SQLException {
        String sql = "INSERT INTO event_subscribers(event_id, user_id) VALUES(?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            for (User u : subscribers) {
                ps.setLong(1, eventId);
                ps.setLong(2, u.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }


    @Override
    public void remove(Long key) throws SQLException {
        String sql = "DELETE FROM events WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, key);
            ps.executeUpdate();
        }
    }

    @Override
    public void modify(Long key, Event entity) throws SQLException {
        remove(key);
        add(key, entity);
    }

    @Override
    public Event find(Long key) throws SQLException {
        String sql = "SELECT id FROM events WHERE id=?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, key);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                List<Duck> ducks = fetchDucks(connection, key);
                List<Culoar> lanes = fetchLanes(connection, key);
                List<User> subs = fetchSubscribers(connection, key);

                DuckRaceContainer container = new DuckRaceContainer(ducks, lanes);
                RaceEvent event = new RaceEvent(key, container);
                event.setSubscribers(subs);
                return event;
            }
        }
    }

    private List<Duck> fetchDucks(Connection connection, Long eventId) throws SQLException {
        String sql = "SELECT duck_id FROM event_ducks WHERE event_id=?";
        List<Duck> ducks = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    long duckId = rs.getLong("duck_id");
                    Duck d = (Duck) userRepo.find(duckId);
                    if (d != null) ducks.add(d);
                }
            }
        }
        return ducks;
    }

    private List<Culoar> fetchLanes(Connection connection, Long eventId) throws SQLException {
        String sql = "SELECT distance, lane_index FROM event_lanes WHERE event_id=?";
        List<Culoar> lanes = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lanes.add(new Culoar(rs.getInt("distance"), rs.getInt("lane_index")));
                }
            }
        }
        return lanes;
    }

    private List<User> fetchSubscribers(Connection connection, Long eventId) throws SQLException {
        String sql = "SELECT user_id FROM event_subscribers WHERE event_id=?";
        List<User> subs = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
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


    @Override
    public Collection<Event> getAll() {
        String sql = "SELECT id FROM events";
        List<Event> events = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                long id = rs.getLong("id");
                Event e = find(id);
                if (e != null) events.add(e);
            }

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }

        return events;
    }

    @Override
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

    @Override
    public Collection<Event> getPage(int offset, int limit) throws SQLException {
        String sql = "SELECT id FROM events ORDER BY id LIMIT ? OFFSET ?";
        List<Event> events = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    long id = rs.getLong("id");
                    Event e = find(id);
                    if (e != null) {
                        events.add(e);
                    }
                }
            }

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }

        return events;
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


