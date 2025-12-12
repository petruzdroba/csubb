package com.ui.controllers;

import com.domain.User;
import com.exceptions.RepositoryException;
import com.service.MessageService;
import com.service.UserService;
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

    public AuthViewController() {}

    public void setUserService(UserService userService) {
        this.service = userService;
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
                openMessageView(loggedInUser);
            } else {
                showError("Invalid credentials");
                passwordField.clear();
            }
        } catch (SQLException | RepositoryException e) {
            showError(e.getMessage());
        }
    }


    private void openMessageView(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/message-view.fxml"));
            Parent root = loader.load();
            String darkTheme = getClass().getResource("/dark-theme.css").toExternalForm();

            MessageViewController controller = loader.getController();
            controller.setUserService(service);
            controller.setMessageService(messageService);
            controller.setLoggedInUser(user);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(darkTheme);

            stage.setScene(scene);
            stage.setTitle("Message Screen");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Cannot load message screen: " + e.getMessage());
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
