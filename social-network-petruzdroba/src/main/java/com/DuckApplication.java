package com;

import com.controllers.UsersViewController;
import com.domain.DataBaseConfig;
import com.repo.CardRepository;
import com.repo.EventRepository;
import com.repo.UserRepository;
import com.repo.FriendshipRepository;
import com.service.CardService;
import com.service.EventService;
import com.service.FriendshipService;
import com.service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class DuckApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/users-view.fxml"));

        Parent root = fxmlLoader.load();

        UsersViewController controller = fxmlLoader.getController();

        DataBaseConfig config = new DataBaseConfig("jdbc:postgresql://localhost:5432/social_network", "sn_user", "sn_pass");

        UserRepository userRepo = new UserRepository(config);
        FriendshipRepository friendshipRepo = new FriendshipRepository(config);
        CardRepository cardRepository = new CardRepository(config);

        UserService userService = new UserService(userRepo, friendshipRepo, cardRepository);

        controller.setUserService(userService);

        Scene scene = new Scene(root);
        stage.setTitle("Duck Screen");
        stage.setScene(scene);
        stage.show();
    }

}
