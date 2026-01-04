package com;

import com.repo.*;
import com.service.*;
import com.ui.controllers.AuthViewController;
import com.ui.controllers.DuckFilterController;
import com.domain.DataBaseConfig;
import com.ui.controllers.FriendshipPagedViewController;
import com.ui.controllers.UserPagedViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {
    private DataBaseConfig config;
    private UserRepository userRepository;
    private MessageRepository messageRepository;
    private RequestRepository requestRepository;
    private NotificationRepository notificationRepository;

    private RequestService requestService;
    private FriendshipService friendshipService;
    private UserService userService;
    private MessageService messageService;
    private NotificationService notificationService;

    @Override
    public void start(Stage primaryStage) throws Exception {
        initServices();

//        openDuckFilterWindow();
//        openUserViewWindow();
//        openFriendshipWindow();

//        openAuthWindow();
//        openAuthWindow();
        openAuthWindow();
    }

    private void initServices() {
        config = new DataBaseConfig(
                "jdbc:postgresql://localhost:5432/social_network",
                "sn_user",
                "sn_pass"
        );

        userRepository = new UserRepository(config);
        messageRepository = new MessageRepository(config, userRepository);

        FriendshipRepository friendshipRepository = new FriendshipRepository(config);

        friendshipService = new FriendshipService(
                friendshipRepository,
                userRepository
        );

        requestRepository = new RequestRepository(config, userRepository);
        notificationRepository = new NotificationRepository(config, userRepository);

        userService = new UserService(
                userRepository,
                friendshipService,
                new CardRepository(config)
        );

        messageService = new MessageService(messageRepository, userRepository);
        notificationService = new NotificationService(notificationRepository);

        requestService = new RequestService(requestRepository, userRepository, friendshipRepository, notificationService);
    }

    private void openDuckFilterWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/duck-filter-view.fxml"));
        Scene scene = new Scene(loader.load());
        DuckFilterController controller = loader.getController();
        controller.setUserService(userService);

        scene.getStylesheets().add(getClass().getResource("/dark-theme.css").toExternalForm());

        Stage stage = new Stage();
        stage.setTitle("Duck Filter");
        stage.setScene(scene);
        stage.show();
    }

    private void openUserViewWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/user-view.fxml"));
        Scene scene = new Scene(loader.load());
        UserPagedViewController controller = loader.getController();
        controller.setUserService(userService);

        scene.getStylesheets().add(getClass().getResource("/dark-theme.css").toExternalForm());

        Stage stage = new Stage();
        stage.setTitle("User View");
        stage.setScene(scene);
        stage.show();
    }

    private void openFriendshipWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/friendship-view.fxml"));
        Scene scene = new Scene(loader.load());
        FriendshipPagedViewController controller = loader.getController();
        controller.setFriendshipService(friendshipService);

        scene.getStylesheets().add(getClass().getResource("/dark-theme.css").toExternalForm());

        Stage stage = new Stage();
        stage.setTitle("Friendship View");
        stage.setScene(scene);
        stage.show();
    }

    private void openAuthWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/auth-view.fxml"));
        Scene scene = new Scene(loader.load());
        AuthViewController controller = loader.getController();
        controller.setUserService(userService);
        controller.setMessageService(messageService);
        controller.setFriendshipService(friendshipService);
        controller.setRequestService(requestService);
        controller.setNotificationService(notificationService);

        scene.getStylesheets().add(getClass().getResource("/dark-theme.css").toExternalForm());

        Stage stage = new Stage();
        stage.setTitle("Login");
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
