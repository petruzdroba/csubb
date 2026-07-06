package org.zdroba.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zdroba.entity.RaceEvent;
import org.zdroba.entity.Racer;
import org.zdroba.entity.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RacerRepositoryImpl implements RacerRepository{

    private static RacerRepositoryImpl instance;

    private RacerRepositoryImpl() {}

    public static RacerRepositoryImpl getInstance() {
        if (instance == null)
            instance = new RacerRepositoryImpl();
        return instance;
    }

    private final RaceEventRepository eventRepository = RaceEventRepositoryImpl.getInstance();
    private final Connection connection = DataBaseConnection.getInstance().getConnection();
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Racer find(Long id) {
        logger.traceEntry();
        String sql = "SELECT * FROM racers WHERE id = ?";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setLong(1,id);

            try(ResultSet rs = ps.executeQuery()){

                if(rs.next()){
                    String name = rs.getString("name");
                    String cnp = rs.getString("cnp");
                    RaceEvent engine = eventRepository.find(rs.getInt("engine"));
                    Team team = Team.valueOf(rs.getString("team"));

                    Racer racer = new Racer(name,cnp,team,engine);
                    racer.setId(id);

                    logger.traceExit();
                    return racer;
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
    public List<Racer> getAll() {
        logger.traceEntry();
        String sql = "SELECT * FROM racers";

        List<Racer> racers = new ArrayList<>();

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){

                while(rs.next()){
                    Long id = rs.getLong("id");
                    String name = rs.getString("name");
                    String cnp = rs.getString("cnp");
                    RaceEvent engine = eventRepository.find(rs.getInt("engine"));
                    Team team = Team.valueOf(rs.getString("team"));

                    Racer racer = new Racer(name,cnp,team,engine);
                    racer.setId(id);

                    racers.add(racer);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            System.err.println(e.getMessage());
        }

        logger.traceExit();
        return racers;
    }

    @Override
    public List<Racer> getBy(Team team) {
        logger.traceEntry();
        String sql = "SELECT * FROM racers WHERE team = ?";

        List<Racer> racers = new ArrayList<>();

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, team.name());

            try(ResultSet rs = ps.executeQuery()){

                while(rs.next()){
                    Long id = rs.getLong("id");
                    String name = rs.getString("name");
                    String cnp = rs.getString("cnp");
                    RaceEvent engine = eventRepository.find(rs.getInt("engine"));

                    Racer racer = new Racer(name,cnp,team,engine);
                    racer.setId(id);

                    racers.add(racer);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            System.err.println(e.getMessage());
        }

        logger.traceExit();
        return racers;
    }

    @Override
    public List<Racer> getBy(RaceEvent engine) {
        logger.traceEntry();
        String sql = "SELECT * FROM racers WHERE engine = ?";

        List<Racer> racers = new ArrayList<>();

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, engine.getEngine());

            try(ResultSet rs = ps.executeQuery()){

                while(rs.next()){
                    Long id = rs.getLong("id");
                    String name = rs.getString("name");
                    String cnp = rs.getString("cnp");
                    Team team = Team.valueOf(rs.getString("team"));

                    Racer racer = new Racer(name,cnp,team,engine);
                    racer.setId(id);

                    racers.add(racer);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            System.err.println(e.getMessage());
        }

        logger.traceExit();
        return racers;
    }

    @Override
    public void add(Racer racer) {
        logger.traceEntry("saving Racer{}", racer);
        String sql = "INSERT INTO racers (name,cnp,engine,team) VALUES (?,?,?,?)";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, racer.getName());
            ps.setString(2, racer.getCnp());
            ps.setInt(3, racer.getEngine().getEngine());
            ps.setString(4, racer.getTeam().name());

            int result = ps.executeUpdate();
            logger.trace("Saved {} instance", result);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            System.err.println(e.getMessage());
        }

        logger.traceExit();
    }

    @Override
    public void modify(Racer racer) {
        logger.traceEntry("Modifying Racer{}", racer.getId());
        String sql = "UPDATE racers SET name = ?, cnp = ?, team = ?, engine = ? WHERE id = ?";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, racer.getName());
            ps.setString(2, racer.getCnp());
            ps.setString(3, racer.getTeam().name());
            ps.setLong(4, racer.getEngine().getId());
            ps.setLong(5, racer.getId());

            int result = ps.executeUpdate();
            logger.trace("Updated {} instance", result);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            System.err.println(e.getMessage());
        }

        logger.traceExit();
    }
}
