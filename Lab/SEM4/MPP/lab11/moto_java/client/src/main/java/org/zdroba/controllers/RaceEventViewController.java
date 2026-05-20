package org.zdroba.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.jspecify.annotations.Nullable;
import org.zdroba.entity.RaceEvent;
import org.zdroba.entity.User;
import org.zdroba.exceptions.AlreadyExistsException;
import org.zdroba.service.RaceEventService;
import org.zdroba.service.RacerService;
import org.zdroba.sync.*;

import java.util.List;

import static org.zdroba.sync.RequestType.RACER_ADD;
import static org.zdroba.sync.RequestType.RACER_UPDATE;

public class RaceEventViewController implements Observer {

    private User loggedInUser;
    private RaceEventService raceEventService;
    private RacerService racerService;
    private SocketNotifier notifier;

    @FXML
    private TableView<RaceEvent> eventTable;
    @FXML
    private TableColumn<RaceEvent, String> engineColumn;
    @FXML
    private TableColumn<RaceEvent, Integer> participantsColumn;
    @FXML
    private TextField engineField;

    public void setRaceEventService(RaceEventService raceEventService) {
        this.raceEventService = raceEventService;
        loadData(raceEventService.getAll());
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public void setRacerService(RacerService racerService) {
        this.racerService = racerService;
    }

    public void setNotifier(SocketNotifier notifier) {
        this.notifier = notifier;

        this.notifier.onUpdate((message) -> {
            Platform.runLater(() -> {
                handleMessage(message);
            });
        });

        this.notifier.start();
    }

    private void loadData(List<RaceEvent> events) {

        engineColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getEngine() + "cc"
                )
        );

        participantsColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        racerService.getAll(data.getValue().getEngine()).size()
                ).asObject()
        );

        eventTable.setItems(FXCollections.observableArrayList(events));
    }

    @FXML
    private void addEvent() {
        try {
            int engine = Integer.parseInt(engineField.getText());

            raceEventService.add(engine);
            loadData(raceEventService.getAll());
        } catch (NumberFormatException e) {
            showError("Engine must be a number!");
        } catch (AlreadyExistsException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showError(e.getMessage());
        } finally {
            engineField.clear();
        }
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
            case EVENT_ADD:
                Platform.runLater(() -> loadData(raceEventService.getAll()));
                break;

            case RACER_UPDATE:
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

    @Override
    public void update(String message) {
        if(raceEventService == null) return;

        System.out.println("Local got "+ notifier.getPort()+" "+message);
        loadData(raceEventService.getAll());
    }
}