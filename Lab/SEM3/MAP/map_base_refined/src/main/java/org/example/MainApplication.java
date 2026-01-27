package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controllers.AuthViewController;
import org.example.controllers.EntityController;
import org.example.domain.DataBaseConfig;
import org.example.domain.Meci;
import org.example.domain.User;
import org.example.repo.DatabaseConnection;
import org.example.repo.EvenimentRepo;
import org.example.repo.MeciRepo;
import org.example.repo.UserRepo;
import org.example.service.EvenimentService;
import org.example.service.MeciService;
import org.example.service.UserService;

public class MainApplication extends Application {
    private DataBaseConfig config;

    private UserRepo userRepo;
    private UserService userService;

    private MeciRepo meciRepo;
    private MeciService meciService;

    private EvenimentRepo evenimentRepo;
    private EvenimentService evenimentService;

    @Override
    public void start(Stage primaryStage) throws Exception {
        config = new DataBaseConfig(
                "jdbc:postgresql://localhost:5432/mappractic",
                "sn_user",
                "sn_pass"
        );

        userRepo = new UserRepo(config);
        userService = new UserService(userRepo);

        meciRepo = new MeciRepo(config);
        meciService = new MeciService(meciRepo);

        evenimentRepo = new EvenimentRepo(config);
        evenimentService = new EvenimentService(evenimentRepo, meciRepo);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/auth.fxml"));
        Scene scene = new Scene(loader.load());
        AuthViewController controller = loader.getController();
        controller.setService(userService);
        controller.setMeciService(meciService);
        controller.setEvenimentService(evenimentService);

        Stage stage = new Stage();
        stage.setTitle("Auth View");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
