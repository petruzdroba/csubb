package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controllers.CompanyViewController;
import org.example.controllers.DriverViewController;
import org.example.domain.DataBaseConfig;
import org.example.domain.Driver;
import org.example.domain.Order;
import org.example.repo.DatabaseConnection;
import org.example.repo.DriverRepository;
import org.example.repo.OrderRepository;
import org.example.repo.RepositoryPaginated;
import org.example.service.DriverService;
import org.example.service.DriverServiceI;
import org.example.service.OrderService;
import org.example.service.OrderServiceI;

public class MainApplication extends Application {
    private DataBaseConfig config;
    private DriverServiceI driverService;
    private OrderServiceI orderService;

    private OrderRepository orderRepo;
    private DriverRepository driverRepo;

    @Override
    public void start(Stage primaryStage) throws Exception {
        config = new DataBaseConfig(
                "jdbc:postgresql://localhost:5432/mappractic",
                "sn_user",
                "sn_pass"
        );

        orderRepo = new OrderRepository(config);
        orderService = new OrderService(orderRepo);

        driverRepo = new DriverRepository(config);
        driverService = new DriverService(driverRepo);

        for(Driver d: driverRepo.getAll()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/driver_view.fxml"));
            Scene scene = new Scene(loader.load());
            DriverViewController controller = loader.getController();
            controller.setOrderService(orderService);
            controller.setDriver(d);

            Stage stage = new Stage();
            stage.setTitle(d.getName());
            stage.setScene(scene);
            stage.show();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/company_view.fxml"));
        Scene scene = new Scene(loader.load());
        CompanyViewController controller = loader.getController();
        controller.setOrderService(orderService);

        Stage stage = new Stage();
        stage.setTitle("Company");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
