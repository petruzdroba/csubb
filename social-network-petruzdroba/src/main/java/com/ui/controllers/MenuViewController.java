package com.ui.controllers;

import com.domain.User;
import com.service.MessageService;
import com.service.UserService;
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
    private User loggedInUser;

    private Stage messageStage;

    @FXML
    private Label userLabel;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
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
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/auth-view.fxml"));
            Parent root = loader.load();

            AuthViewController controller = loader.getController();
            controller.setUserService(userService);
            controller.setMessageService(messageService);

            if (loggedInUser != null) {
                messageService.removeObserver(loggedInUser);
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
