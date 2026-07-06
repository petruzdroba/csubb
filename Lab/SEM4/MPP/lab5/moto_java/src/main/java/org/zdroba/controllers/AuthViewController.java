package org.zdroba.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.zdroba.entity.User;
import org.zdroba.exceptions.AlreadyExistsException;
import org.zdroba.exceptions.InvalidPasswordException;
import org.zdroba.exceptions.NotFoundException;
import org.zdroba.service.AuthService;
import org.zdroba.service.RaceEventService;
import org.zdroba.service.RacerService;

import java.io.IOException;

public class AuthViewController {

    private User loggedInUser = null;
    private AuthService authService;

    //not used specifically here, but passed to other controllers
    private RacerService racerService;
    private RaceEventService raceEventService;

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    public void setRacerService(RacerService racerService) {
        this.racerService = racerService;
    }

    public void setRaceEventService(RaceEventService raceEventService) {
        this.raceEventService = raceEventService;
    }

    @FXML private TextField loginEmailField;
    @FXML private PasswordField loginPasswordField;

    @FXML private TextField registerEmailField;
    @FXML private PasswordField registerPasswordField;

    @FXML private void logIn() {
        try {
            String email = loginEmailField.getText();
            String password = loginPasswordField.getText();

            loggedInUser = authService.logIn(email, password);
            System.out.println(loggedInUser);

            openRacerView(loggedInUser);
            openRaceEventView(loggedInUser);
        } catch (InvalidPasswordException | NotFoundException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showError("Unexpected error: " + e.getMessage());
        }finally{
            loginEmailField.clear();
            loginPasswordField.clear();
        }
    }

    @FXML private void register() {
        try {
            String email = registerEmailField.getText();
            String password = registerPasswordField.getText();

            loggedInUser = authService.register(email, password);
            System.out.println(loggedInUser);

            openRacerView(loggedInUser);
            openRaceEventView(loggedInUser);
        } catch (AlreadyExistsException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showError("Unexpected error: " + e.getMessage());
        }finally{
            registerEmailField.clear();
            registerPasswordField.clear();
        }
    }

    protected void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openRacerView(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/racer-view.fxml"));
        Scene scene = new Scene(loader.load());
        RacerViewController controller = loader.getController();

        controller.setLoggedInUser(user);
        controller.setRacerService(racerService);

        Stage stage = new Stage();
        stage.setTitle("Racer View");
        stage.setScene(scene);
        stage.show();
    }

    private void openRaceEventView(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/race-event-view.fxml"));
        Scene scene = new Scene(loader.load());
        RaceEventViewController controller = loader.getController();

        controller.setLoggedInUser(user);
        controller.setRacerService(racerService);
        controller.setRaceEventService(raceEventService);

        Stage stage = new Stage();
        stage.setTitle("Race Event View");
        stage.setScene(scene);
        stage.show();
    }
}