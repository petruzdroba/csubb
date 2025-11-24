package com;

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
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    public static void main(String[] args) {
        DataBaseConfig config = new DataBaseConfig("jdbc:postgresql://localhost:5432/social_network", "sn_user", "sn_pass");

        UserRepository userRepo = new UserRepository(config);
        FriendshipRepository friendshipRepo = new FriendshipRepository(config);
        CardRepository cardRepository = new CardRepository(config);
        EventRepository eventRepository = new EventRepository(config, userRepo);

        UserService userService = new UserService(userRepo, friendshipRepo, cardRepository);
        FriendshipService friendshipService = new FriendshipService(friendshipRepo, userRepo);
        CardService cardService = new CardService(cardRepository, userRepo);
        EventService eventService = new EventService(eventRepository, cardService, userRepo);

        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/duck-view.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Test JavaFX Screen");
        stage.setScene(scene);
        stage.show();
    }
}
