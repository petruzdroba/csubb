package org.zdroba.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zdroba.entity.RaceEvent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RaceEventRepositoryImpl implements RaceEventRepository {

    private static RaceEventRepositoryImpl instance = null;

    private RaceEventRepositoryImpl() {}

    public static RaceEventRepositoryImpl getInstance() {
        if (instance == null)
            instance = new RaceEventRepositoryImpl();
        return instance;
    }

    private final Connection connection = DataBaseConnection.getInstance().getConnection();
    private static final Logger logger = LogManager.getLogger();

    @Override
    public RaceEvent find(Long key) {
        logger.traceEntry();
        String sql = "SELECT * FROM events WHERE id = ? LIMIT 1";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setLong(1, key);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    int engine = rs.getInt("engine");

                    RaceEvent event = new RaceEvent(engine);
                    event.setId(key);

                    logger.traceExit();
                    return event;
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            System.err.println(e.getMessage());
        }

        logger.traceExit();
        return null;
    }

    @Override
    public List<RaceEvent> getAll() {
        logger.traceEntry();
        String sql = "SELECT * FROM events";

        List<RaceEvent> events = new ArrayList<>();

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){

                while(rs.next()){
                    Long id = rs.getLong("id");
                    int engine = rs.getInt("engine");

                    RaceEvent event = new RaceEvent(engine);
                    event.setId(id);

                    events.add(event);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            System.err.println(e.getMessage());
        }

        logger.traceExit();
        return events;
    }

    @Override
    public void add(RaceEvent raceEvent) {
        logger.traceEntry("saving RaceEvent{}", raceEvent);
        String sql = "INSERT INTO events (engine) VALUES (?)";

        try(PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, raceEvent.getEngine());

            int result = ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                raceEvent.setId(rs.getLong(1));
            }

            logger.trace("Saved {} instance ", result);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            System.err.println(e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public RaceEvent find(int engine) {
        logger.traceEntry();
        String sql = "SELECT * FROM events WHERE engine = ? LIMIT 1";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, engine);

            try(ResultSet rs = ps.executeQuery()){

                if(rs.next()){
                    Long id = rs.getLong("id");

                    RaceEvent event = new RaceEvent(engine);
                    event.setId(id);

                    logger.traceExit();
                    return event;
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            System.err.println(e.getMessage());
        }

        logger.traceExit();
        return null;
    }
}
