package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.domain.DataBaseConfig;
import org.example.repo.DatabaseConnection;

public class MainApplication extends Application {
    private DataBaseConfig config;

    @Override
    public void start(Stage stage) throws Exception {
        config = new DataBaseConfig(
                "jdbc:postgresql://localhost:5432/social_network",
                "sn_user",
                "sn_pass"
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}
