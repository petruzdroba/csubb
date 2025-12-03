package com;

import com.ui.controllers.DuckFilterController;
import com.domain.DataBaseConfig;
import com.repo.CardRepository;
import com.repo.UserRepository;
import com.repo.FriendshipRepository;
import com.service.UserService;
import com.ui.controllers.UserViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/duck-filter-view.fxml"));
        Scene duckScene = new Scene(loader.load());
        DuckFilterController controller = loader.getController();

        FXMLLoader userLoader = new FXMLLoader(getClass().getResource("/user-view.fxml"));
        Scene userScene = new Scene(userLoader.load());
        UserViewController userController = userLoader.getController();

        DataBaseConfig config = new DataBaseConfig(
                "jdbc:postgresql://localhost:5432/social_network",
                "sn_user",
                "sn_pass"
        );
        UserService userService = new UserService(
                new UserRepository(config),
                new FriendshipRepository(config),
                new CardRepository(config)
        );

        controller.setUserService(userService);
        Stage duckStage = new Stage();
        duckStage.setTitle("Duck Filter");
        duckStage.setScene(duckScene);
        duckStage.show();

        userController.setUserService(userService);
        Stage userStage = new Stage();
        userStage.setTitle("User View");
        userStage.setScene(userScene);
        userStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
