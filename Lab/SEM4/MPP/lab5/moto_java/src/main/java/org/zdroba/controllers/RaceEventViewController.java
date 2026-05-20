package org.zdroba.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.zdroba.entity.RaceEvent;
import org.zdroba.entity.User;
import org.zdroba.exceptions.AlreadyExistsException;
import org.zdroba.service.RaceEventService;
import org.zdroba.service.RacerService;

import java.util.List;

public class RaceEventViewController {

    private User loggedInUser;
    private RaceEventService raceEventService;
    private RacerService racerService;

    @FXML private TableView<RaceEvent> eventTable;
    @FXML private TableColumn<RaceEvent, String> engineColumn;
    @FXML private TableColumn<RaceEvent, Integer> participantsColumn;
    @FXML private TextField engineField;

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
        }catch (Exception e) {
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
}