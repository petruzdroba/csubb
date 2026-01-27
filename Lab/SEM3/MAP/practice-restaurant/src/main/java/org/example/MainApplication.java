package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controllers.CustomerViewController;
import org.example.controllers.StaffViewController;
import org.example.domain.DataBaseConfig;
import org.example.domain.Table;
import org.example.repo.MenuItemRepo;
import org.example.repo.OrderRepo;
import org.example.repo.TableRepo;
import org.example.service.MenuService;
import org.example.service.OrderService;
import org.example.service.TableService;

import java.io.IOException;

public class MainApplication extends Application {
    private DataBaseConfig config;

    private TableRepo tableRepo;
    private TableService tableService;

    private MenuItemRepo menuItemRepo;
    private MenuService menuService;

    private OrderRepo orderRepo;
    private OrderService orderService;

    @Override
    public void start(Stage primaryStage) throws Exception {
        config = new DataBaseConfig(
                "jdbc:postgresql://localhost:5432/mappractic",
                "sn_user",
                "sn_pass"
        );

        tableRepo = new TableRepo(config);
        tableService = new TableService(tableRepo);

        menuItemRepo = new MenuItemRepo(config);
        menuService = new MenuService(menuItemRepo);

        orderRepo = new OrderRepo(config, menuItemRepo);
        orderService = new OrderService(orderRepo);

        for (Table table : tableService.getAll()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/customer-view.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(loader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            CustomerViewController controller = loader.getController();
            controller.setMenuService(menuService);
            controller.setOrderService(orderService);
            controller.setTable(table);

            Stage stage = new Stage();
            stage.setTitle("Table " + table.getId());
            stage.setScene(scene);
            stage.show();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/staff-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        StaffViewController controller = loader.getController();
        controller.setOrderService(orderService);

        Stage stage = new Stage();
        stage.setTitle("Staff");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
