package com;

import com.service.FriendshipService;
import com.ui.controllers.DuckFilterController;
import com.domain.DataBaseConfig;
import com.repo.CardRepository;
import com.repo.UserRepository;
import com.repo.FriendshipRepository;
import com.service.UserService;
import com.ui.controllers.FriendshipViewController;
import com.ui.controllers.UserViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/duck-filter-view.fxml"));
        Scene duckScene = new Scene(loader.load());
        DuckFilterController controller = loader.getController();

        FXMLLoader userLoader = new FXMLLoader(getClass().getResource("/user-view.fxml"));
        Scene userScene = new Scene(userLoader.load());
        UserViewController userController = userLoader.getController();

        FXMLLoader friendshipLoader = new FXMLLoader(getClass().getResource("/friendship-view.fxml"));
        Scene friendshipScene = new Scene(friendshipLoader.load());
        FriendshipViewController friendshipController = friendshipLoader.getController();

        String darkTheme = getClass().getResource("/dark-theme.css").toExternalForm();
        duckScene.getStylesheets().add(darkTheme);
        userScene.getStylesheets().add(darkTheme);
        friendshipScene.getStylesheets().add(darkTheme);

        DataBaseConfig config = new DataBaseConfig(
                "jdbc:postgresql://localhost:5432/social_network",
                "sn_user",
                "sn_pass"
        );
        UserRepository userRepository = new UserRepository(config);

        FriendshipService friendshipService = new FriendshipService(
                new FriendshipRepository(config),
                userRepository
        );

        UserService userService = new UserService(
                userRepository,
                friendshipService,
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

        friendshipController.setFriendshipService(friendshipService);
        Stage friendshipStage = new Stage();
        friendshipStage.setTitle("Friendship View");
        friendshipStage.setScene(friendshipScene);
        friendshipStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
