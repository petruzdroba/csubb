package org.zdroba;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CarsDBRepository implements CarRepository {

    private JdbcUtils dbUtils;


    private static final Logger logger = LogManager.getLogger();

    public CarsDBRepository(Properties props) {
        logger.info("Initializing CarsDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public List<Car> findByManufacturer(String manufacturerN) {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        String sql = "SELECT * FROM cars WHERE manufacturer=?";
        List<Car> cars = new ArrayList<>();

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1,manufacturerN);

            try(ResultSet rs = ps.executeQuery()){

                while(rs.next()){
                    int id = rs.getInt("id");
                    String manufacturer = rs.getString("manufacturer");
                    String model = rs.getString("model");
                    int year = rs.getInt("year");

                    Car car = new Car(manufacturer,model, year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            System.err.println(e);
        }

        logger.traceExit();
        return cars;
    }

    @Override
    public List<Car> findBetweenYears(int min, int max) {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        String sql = "SELECT * FROM cars WHERE year BETWEEN (?) AND (?)";
        List<Car> cars = new ArrayList<>();

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,min);
            ps.setInt(2,max);

            try(ResultSet rs = ps.executeQuery()){

                while(rs.next()){
                    int id = rs.getInt("id");
                    String manufacturer = rs.getString("manufacturer");
                    String model = rs.getString("model");
                    int year = rs.getInt("year");

                    Car car = new Car(manufacturer,model, year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            System.err.println(e);
        }

        logger.traceExit();
        return cars;
    }

    @Override
    public void add(Car elem) {
        logger.traceEntry("saving task{}", elem);
        Connection connection = dbUtils.getConnection();
        String sql = "INSERT INTO cars (manufacturer, model, year) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, elem.getManufacturer());
            ps.setString(2, elem.getModel());
            ps.setInt(3, elem.getYear());

            int result = ps.executeUpdate();
            logger.trace("Saved {} instance ", result);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Car elem) {
        //to do
    }

    @Override
    public Iterable<Car> findAll() {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        String sql = "SELECT * FROM cars";
        List<Car> cars = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            try(ResultSet rs = ps.executeQuery()){

                while(rs.next()){
                    int id = rs.getInt("id");
                    String manufacturer = rs.getString("manufacturer");
                    String model = rs.getString("model");
                    int year = rs.getInt("year");

                    Car car = new Car(manufacturer,model, year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
        return cars;
    }
}
