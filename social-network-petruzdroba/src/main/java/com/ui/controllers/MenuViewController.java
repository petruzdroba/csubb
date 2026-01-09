package com.ui.controllers;

import com.domain.User;
import com.service.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuViewController {

    private UserService userService;
    private MessageService messageService;
    private FriendshipService friendshipService;
    private RequestService requestService;
    private NotificationService notificationService;
    private ProfilePictureService profilePictureService;
    private RaceEventService raceEventService;
    private User loggedInUser;

    private Stage messageStage;
    private Stage friendsStage;
    private Stage profileStage;
    private Stage raceStage;


    @FXML
    private Label userLabel;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    public void setRequestService(RequestService requestService) {
        this.requestService = requestService;
    }

    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void setProfilePictureService(ProfilePictureService profilePictureService) {
        this.profilePictureService = profilePictureService;
    }

    public void setRaceEventService(RaceEventService raceEventService){
        this.raceEventService = raceEventService;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        userLabel.setText(user.getUsername() + " (" + user.getEmail() + ")");
    }

    @FXML
    private void openMessages() {
        if (messageStage != null) {
            messageStage.toFront();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/message-view.fxml"));
            Parent root = loader.load();

            MessageViewController controller = loader.getController();
            controller.setUserService(userService);
            controller.setMessageService(messageService);
            controller.setLoggedInUser(loggedInUser);

            messageStage = new Stage();
            messageStage.setTitle("Messages");

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/dark-theme.css").toExternalForm()
            );

            messageStage.setScene(scene);
            messageStage.setOnCloseRequest(e -> messageStage = null);
            messageStage.show();

        } catch (IOException e) {
            showError("Cannot open messages: " + e.getMessage());
        }
    }

    @FXML
    private void openFriends(){
        if (friendsStage != null) {
            friendsStage.toFront();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/request-view.fxml"));
            Parent root = loader.load();

            RequestViewController controller = loader.getController();
            controller.setFriendshipService(friendshipService);
            controller.setRequestService(requestService);
            controller.setNotificationService(notificationService);

            controller.setUserService(userService);
            controller.setMessageService(messageService);
            controller.setProfilePictureService(profilePictureService);

            controller.setLoggedInUser(loggedInUser);

            friendsStage = new Stage();
            friendsStage.setTitle("Frienships");

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/dark-theme.css").toExternalForm()
            );

            friendsStage.setScene(scene);
            friendsStage.setOnCloseRequest(e -> friendsStage = null);
            friendsStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Cannot open friends view: " + e.getMessage());
        }
    }

    @FXML
    private void openProfile() {
        if (profileStage != null) {
            profileStage.toFront();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile-page-view.fxml"));
            Parent root = loader.load();

            ProfilePageViewController controller = loader.getController();
            controller.setUserService(userService);
            controller.setMessageService(messageService);
            controller.setRequestService(requestService);
            controller.setFriendshipService(friendshipService);
            controller.setProfilePictureService(profilePictureService);

            controller.setLoggedInUser(loggedInUser);
            controller.setDisplayUser(loggedInUser);

            profileStage = new Stage();
            profileStage.setTitle("Profile");

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/dark-theme.css").toExternalForm()
            );

            profileStage.setScene(scene);
            profileStage.setOnCloseRequest(e ->{ profileStage = null; controller.removeObservers();});
            profileStage.show();

        } catch (IOException e) {
            showError("Cannot open profile: " + e.getMessage());
        }
    }

    @FXML
    private void openRace(){
        if (raceStage != null) {
            raceStage.toFront();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/race-event-view.fxml"));
            Parent root = loader.load();

            RaceEventViewController controller = loader.getController();

            controller.setRaceEventService(raceEventService);
            controller.setUserService(userService);
            controller.setLoggedInUser(loggedInUser);

            raceStage = new Stage();
            raceStage.setTitle("Race Events");

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/dark-theme.css").toExternalForm()
            );

            raceStage.setScene(scene);
            raceStage.setOnCloseRequest(e ->{ raceStage = null; controller.removeObservers();});
            raceStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Cannot open race: " + e.getMessage());
        }
    }


    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/auth-view.fxml"));
            Parent root = loader.load();

            AuthViewController controller = loader.getController();
            controller.setUserService(userService);
            controller.setMessageService(messageService);
            controller.setFriendshipService(friendshipService);
            controller.setRequestService(requestService);
            controller.setNotificationService(notificationService);
            controller.setProfilePictureService(profilePictureService);
            controller.setRaceEventService(raceEventService);

            if (loggedInUser != null) {
                messageService.removeObserver(loggedInUser);
                requestService.removeObserver(loggedInUser);
            }

            Stage stage = (Stage) userLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.sizeToScene();
            stage.show();

            if (messageStage != null) {
                messageStage.close();
                messageStage = null;
            }

        } catch (IOException e) {
            showError("Cannot load login screen: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
