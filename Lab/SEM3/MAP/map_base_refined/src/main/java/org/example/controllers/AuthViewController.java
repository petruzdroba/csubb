package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.domain.User;
import org.example.service.EvenimentService;
import org.example.service.MeciService;
import org.example.service.UserService;

import java.io.IOException;
import java.sql.SQLException;

public class AuthViewController {
    private UserService service;
    private MeciService meciService;
    private User loggedInuser;

    private EvenimentService evenimentService;

    public void setEvenimentService(EvenimentService evenimentService) {
        this.evenimentService = evenimentService;
    }

    public void setService(UserService service) {
        this.service = service;
    }

    public void setMeciService(MeciService meciService) {
        this.meciService = meciService;
    }

    @FXML
    private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;

    @FXML
    private void logIn() {
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();

            User loggedInUser = service.logIn(username, password);

            if (loggedInUser != null) {
                openWindow(loggedInUser);
            } else {
                showError("Invalid credentials");
                passwordField.clear();
            }
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void openWindow(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main-view.fxml"));
        Scene scene = new Scene(loader.load());
        EntityController controller = loader.getController();
        controller.setMeciService(meciService);
        controller.setLoggedInUser(user);
        controller.setEvenimentService(evenimentService);

        Stage stage = new Stage();
        stage.setTitle("Main View");
        stage.setScene(scene);
        stage.show();
    }


    protected void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
