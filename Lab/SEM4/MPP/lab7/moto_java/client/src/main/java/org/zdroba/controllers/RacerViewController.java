package org.zdroba.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jspecify.annotations.Nullable;
import org.zdroba.entity.Racer;
import org.zdroba.entity.Team;
import org.zdroba.entity.User;
import org.zdroba.exceptions.AlreadyExistsException;
import org.zdroba.exceptions.NotFoundException;
import org.zdroba.service.RacerService;
import org.zdroba.sync.Request;
import org.zdroba.sync.Response;
import org.zdroba.sync.ResponseType;
import org.zdroba.sync.SocketNotifier;

import java.util.List;

import static org.zdroba.sync.RequestType.*;

public class RacerViewController {

    private User loggedInUser = null;
    private RacerService racerService;
    private SocketNotifier notifier;

    public void setRacerService(RacerService racerService) {
        this.racerService = racerService;
        loadData(racerService.getAll());
    }

    public void setNotifier(SocketNotifier notifier) {
        this.notifier = notifier;

        this.notifier.onUpdate((message)->{
            Platform.runLater(()->{
                handleMessage(message);
            });
        });

        this.notifier.start();
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    @FXML private TableView<Racer> racerTable;
    @FXML private TableColumn<Racer, String> nameColumn;
    @FXML private TableColumn<Racer, String> cnpColumn;
    @FXML private TableColumn<Racer, Integer> engineColumn;
    @FXML private TableColumn<Racer, String> teamColumn;
    @FXML private ComboBox<String> teamComboBox;

    @FXML private TextField nameField;
    @FXML private TextField cnpField;
    @FXML private TextField engineField;
    @FXML private ComboBox<String> teamFormComboBox;

    @FXML private TextField modifyNameField;
    @FXML private TextField modifyCnpField;
    @FXML private TextField modifyEngineField;
    @FXML private ComboBox<String> modifyTeamComboBox;

    @FXML
    private void initialize() {
        List<String> teams = new java.util.ArrayList<>();
        teams.add("ALL");
        for (Team t : Team.values()) teams.add(t.name());
        teamComboBox.setItems(FXCollections.observableArrayList(teams));
        teamComboBox.setValue("ALL");

        teamFormComboBox.setItems(FXCollections.observableArrayList(
                java.util.Arrays.stream(Team.values()).map(Team::name).toList()
        ));

        modifyTeamComboBox.setItems(FXCollections.observableArrayList(
                java.util.Arrays.stream(Team.values()).map(Team::name).toList()
        ));

        racerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selected) -> {
            if (selected != null) {
                modifyNameField.setText(selected.getName());
                modifyCnpField.setText(selected.getCnp());
                modifyEngineField.setText(String.valueOf(selected.getEngine().getEngine()));
                modifyTeamComboBox.setValue(selected.getTeam().name());
            }
        });
    }

    private void loadData(List<Racer> racers) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        cnpColumn.setCellValueFactory(new PropertyValueFactory<>("cnp"));
        engineColumn.setCellValueFactory(new PropertyValueFactory<>("engine"));
        teamColumn.setCellValueFactory(new PropertyValueFactory<>("team"));

        racerTable.setItems(FXCollections.observableArrayList(racers));
    }

    @FXML
    private void saveRacer() {

        try {
            String name = nameField.getText();
            String cnp = cnpField.getText();
            Integer engine =Integer.parseInt(engineField.getText());
            Team team = Team.valueOf(teamFormComboBox.getValue());

            racerService.add(name,cnp,engine,team);
            loadData(racerService.getAll());
        } catch (AlreadyExistsException|NotFoundException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showError(e.getMessage());
        }finally {
            nameField.clear();
            cnpField.clear();
            engineField.clear();
        }
    }

    @FXML private void modifyRacer() {
        Racer selected = racerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a racer to modify!");
            return;
        }
        try {
            Team team = Team.valueOf(modifyTeamComboBox.getValue());

            racerService.modify(selected.getId(), team);
            loadData(racerService.getAll());
        } catch (NotFoundException e) {
            showError(e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            showError(e.getMessage());
        }finally {
            modifyNameField.clear();
            modifyCnpField.clear();
            modifyEngineField.clear();
            modifyTeamComboBox.setValue(null);
            racerTable.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void searchByTeam() {
        String selected = teamComboBox.getValue();
        if (selected == null || selected.equals("ALL")) {
            loadData(racerService.getAll());
            return;
        }

        Team team = Team.valueOf(selected);
        loadData(racerService.find(team));
    }

    protected void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void handleMessage(String message) {
        if (treatResponse(message)) return;

        Request request = getRequest(message);
        if (request == null) return;

        switch(request.getType()) {
            case RACER_ADD:
            case RACER_UPDATE:
                Platform.runLater(() -> loadData(racerService.getAll()));
                break;

            case EVENT_ADD:
                Platform.runLater(() ->
                        System.out.println("Event added: " + request.getMessage())
                );
                break;

            case AUTH_LOGIN:
            case AUTH_LOGOUT:
                Platform.runLater(() ->
                        System.out.println("Auth response: " + request.getMessage())
                );
                break;

            default:
                System.out.println("Unknown request: " + request);
        }

        notifier.respond(request.getType().name(), ResponseType.OK, "Processed successfully");
    }

    private @Nullable Request getRequest(String message) {
        Request request;
        try {
            request = Request.fromString(message);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid request format: " + message);
            notifier.respond("Invalid", ResponseType.ERROR, "Invalid Format");
            return null;
        }
        return request;
    }

    private static boolean treatResponse(String message) {
        if (message.startsWith("RESPONSE:")) {
            try {
                Response response;
                response = Response.fromString(message);
                System.out.println("[Response] " +
                        response.getRequestType() + " -> " +
                        response.getType() + " : " +
                        response.getMessage());
            } catch (Exception e) {
                System.err.println("Invalid response format: " + message);
            }
            return true;
        }
        return false;
    }
}