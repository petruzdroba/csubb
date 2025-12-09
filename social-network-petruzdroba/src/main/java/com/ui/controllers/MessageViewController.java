package com.ui.controllers;

import com.domain.CurrentUser;
import com.domain.User;
import com.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MessageViewController {

    private UserService service;

    public MessageViewController() {}

    public void setUserService(UserService userService) {
        this.service = userService;
    }

    @FXML private Button logOutButton;
    @FXML private Label userLabel;

    @FXML
    public void initialize(){
        User currentUser = CurrentUser.getInstance().getUser();
        if(currentUser != null) {
            userLabel.setText(currentUser.getUsername() + " (" + currentUser.getEmail() + ")");
        }
    }

    @FXML
    private void handleLogout() {
        System.out.println("Logging out: "+CurrentUser.getInstance().getUser());
        service.logOut();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/auth-view.fxml"));
            Parent root = loader.load();

            AuthViewController controller = loader.getController();
            controller.setUserService(service);

            Stage stage = (Stage) logOutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.sizeToScene();
            stage.show();
        } catch (IOException e) {
            showError("Cannot load login screen: " + e.getMessage());
        }
    }

    protected void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
