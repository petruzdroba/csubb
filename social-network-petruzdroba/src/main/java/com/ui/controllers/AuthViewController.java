package com.ui.controllers;

import com.domain.User;
import com.exceptions.RepositoryException;
import com.service.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class AuthViewController {
    private UserService service;
    private MessageService messageService;
    private RequestService requestService;
    private FriendshipService friendshipService;
    private NotificationService notificationService;
    private ProfilePictureService profilePictureService;
    private RaceEventService raceEventService;

    public AuthViewController() {}

    public void setUserService(UserService userService) {
        this.service = userService;
    }

    public void setRequestService(RequestService service) {
        this.requestService = service;
    }

    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void setProfilePictureService(ProfilePictureService profilePictureService) {
        this.profilePictureService = profilePictureService;
    }

    public void setRaceEventService(RaceEventService raceEventService) {
        this.raceEventService = raceEventService;
    }

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;

    @FXML
    private void logIn() {
        try {
            String email = emailField.getText();
            String password = passwordField.getText();

            User loggedInUser = service.logIn(email, password);

            if (loggedInUser != null) {
                openMenuView(loggedInUser);
            } else {
                showError("Invalid credentials");
                passwordField.clear();
            }
        } catch (SQLException | RepositoryException e) {
            showError(e.getMessage());
        }
    }

    private void openMenuView(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/menu-view.fxml"));
            Parent root = loader.load();

            MenuViewController controller = loader.getController();
            controller.setUserService(service);
            controller.setMessageService(messageService);
            controller.setFriendshipService(friendshipService);
            controller.setRequestService(requestService);
            controller.setNotificationService(notificationService);
            controller.setProfilePictureService(profilePictureService);
            controller.setRaceEventService(raceEventService);
            controller.setLoggedInUser(user);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/dark-theme.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.setTitle("Main Menu");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Cannot load menu");
        }
    }

    protected void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }
}
