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

    @Override
    public void add(Long key, Event entity) throws SQLException {
        if (!(entity instanceof RaceEvent re)) throw new IllegalArgumentException("Only RaceEvent supported");

        String insertEvent = "INSERT INTO events(id) VALUES(?)";
        String insertDucks = "INSERT INTO event_ducks(event_id, duck_id) VALUES(?, ?)";
        String insertLanes = "INSERT INTO event_lanes(event_id, distance, lane_index) VALUES(?, ?, ?)";
        String insertSubs = "INSERT INTO event_subscribers(event_id, user_id) VALUES(?, ?)";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psEvent = conn.prepareStatement(insertEvent);
                 PreparedStatement psDucks = conn.prepareStatement(insertDucks);
                 PreparedStatement psLanes = conn.prepareStatement(insertLanes);
                 PreparedStatement psSubs = conn.prepareStatement(insertSubs)) {

                // Insert event
                psEvent.setLong(1, re.getId());
                psEvent.executeUpdate();

                // Insert ducks
                for (Duck d : re.getContainer().getDucks()) {
                    psDucks.setLong(1, re.getId());
                    psDucks.setLong(2, d.getId());
                    psDucks.addBatch();
                }
                psDucks.executeBatch();

                // Insert culoars
                for (Culoar lane : re.getContainer().getCuloare()) {
                    psLanes.setLong(1, re.getId());
                    psLanes.setInt(2, lane.getDistanta());
                    psLanes.setInt(3, lane.getId());
                    psLanes.addBatch();
                }
                psLanes.executeBatch();

                // Insert subscribers
                for (User u : re.getSubscribers()) {
                    psSubs.setLong(1, re.getId());
                    psSubs.setLong(2, u.getId());
                    psSubs.addBatch();
                }
                psSubs.executeBatch();

                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
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
        String eventQuery = "SELECT id FROM events WHERE id=?";
        String ducksQuery = "SELECT duck_id FROM event_ducks WHERE event_id=?";
        String lanesQuery = "SELECT distance, lane_index FROM event_lanes WHERE event_id=?";
        String subsQuery = "SELECT user_id FROM event_subscribers WHERE event_id=?";

        try (Connection conn = getConnection();
             PreparedStatement psEvent = conn.prepareStatement(eventQuery)) {

            psEvent.setLong(1, key);
            try (ResultSet rsEvent = psEvent.executeQuery()) {
                if (!rsEvent.next()) return null;

                // Fetch ducks
                List<Duck> ducks = new ArrayList<>();
                try (PreparedStatement psDucks = conn.prepareStatement(ducksQuery)) {
                    psDucks.setLong(1, key);
                    try (ResultSet rsDucks = psDucks.executeQuery()) {
                        while (rsDucks.next()) {
                            long duckId = rsDucks.getLong("duck_id");
                            Duck d = (Duck) userRepo.find(duckId);
                            if (d != null) ducks.add(d);
                        }
                    }
                }

                // Fetch lanes
                List<Culoar> lanes = new ArrayList<>();
                try (PreparedStatement psLanes = conn.prepareStatement(lanesQuery)) {
                    psLanes.setLong(1, key);
                    try (ResultSet rsLanes = psLanes.executeQuery()) {
                        while (rsLanes.next()) {
                            lanes.add(new Culoar(rsLanes.getInt("distance"), rsLanes.getInt("lane_index")));
                        }
                    }
                }

                // Fetch subscribers
                List<User> subs = new ArrayList<>();
                try (PreparedStatement psSubs = conn.prepareStatement(subsQuery)) {
                    psSubs.setLong(1, key);
                    try (ResultSet rsSubs = psSubs.executeQuery()) {
                        while (rsSubs.next()) {
                            User u = userRepo.find(rsSubs.getLong("user_id"));
                            if (u != null) subs.add(u);
                        }
                    }
                }

                DuckRaceContainer container = new DuckRaceContainer(ducks, lanes);
                RaceEvent event = new RaceEvent(key, container);
                event.setSubscribers(subs);
                return event;
            }
        }
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


