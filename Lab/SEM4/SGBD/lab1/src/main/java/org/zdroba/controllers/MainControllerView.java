package org.zdroba.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import org.zdroba.entity.Park;
import org.zdroba.entity.Tag;
import org.zdroba.entity.Trail;
import org.zdroba.repository.*;

public class MainControllerView {

    private final IParkRepository parkRepository = ParkRepository.getInstance();
    private final ITrailRepository trailRepository = TrailRepository.getInstance();
    private final ITagRepository tagRepository = TagRepository.getInstance();

    @FXML private TableView<Park> parksTable;
    @FXML private TableColumn<Park, Long> parkIdCol;
    @FXML private TableColumn<Park, String> parkNameCol;
    @FXML private TableColumn<Park, String> parkCountryCol;


    @FXML private TableView<Trail> trailsTable;
    @FXML private TableColumn<Trail, Long> trailIdCol;
    @FXML private TableColumn<Trail, String> trailNameCol;
    @FXML private TableColumn<Trail, Double> trailLengthCol;

    @FXML private TableView<Tag> tagsTable;
    @FXML private TableColumn<Tag, Long> tagIdCol;
    @FXML private TableColumn<Tag, String> tagNameCol;

    private final ObservableList<Park> parkList = FXCollections.observableArrayList();
    private final ObservableList<Trail> trailList = FXCollections.observableArrayList();
    private final ObservableList<Tag> tagList = FXCollections.observableArrayList();

    @FXML private TextField trailNameField;
    @FXML private TextField trailLengthField;

    @FXML private TextField tagNameField;

    @FXML
    public void initialize() {
        parkIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        parkNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        parkCountryCol.setCellValueFactory(new PropertyValueFactory<>("county"));

        trailIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        trailNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        trailLengthCol.setCellValueFactory(new PropertyValueFactory<>("length"));

        tagIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        tagNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        parksTable.setItems(parkList);
        trailsTable.setItems(trailList);
        tagsTable.setItems(tagList);

        parksTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> onParkSelected(newVal)
        );

        loadParks();
        loadTags();
    }

    private void loadParks() {
        parkList.setAll(parkRepository.getAll());
        if (!parkList.isEmpty()) {
            parksTable.getSelectionModel().selectFirst();
        }
    }

    private void loadTags() {
        tagList.setAll(tagRepository.getAll());
    }

    private void onParkSelected(Park park) {
        if (park == null) return;
        trailList.setAll(
                trailRepository.getAll().stream()
                        .filter(t -> t.getPark().getId().equals(park.getId()))
                        .toList()
        );
    }

    @FXML private void onAddPark() {
        Dialog<Park> dialog = new Dialog<>();
        dialog.setTitle("Add Park");

        GridPane grid = new GridPane();

        TextField nameField = new TextField();
        TextField countryField = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Country:"), 0, 1);
        grid.add(countryField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                String name = nameField.getText().trim();
                String country = countryField.getText().trim();
                if (name.isEmpty() || country.isEmpty()) {
                    showError("Park attributes cannot be empty");
                    return null;
                }
                return new Park(name, country);
            }
            return null;
        });

        dialog.showAndWait().ifPresent(park -> {
            parkRepository.add(park);
            loadParks();
        });
    }

    @FXML private void onEditPark() {
        Dialog<Park> dialog = new Dialog<>();
        dialog.setTitle("Edit Park");

        GridPane grid = new GridPane();

        Park selected = parksTable.getSelectionModel().getSelectedItem();
        TextField nameField = new TextField(selected.getName());
        TextField countryField = new TextField(selected.getCounty());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Country:"), 0, 1);
        grid.add(countryField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                String name = nameField.getText().trim();
                String country = countryField.getText().trim();
                if (name.isEmpty() || country.isEmpty()) {
                    showError("Park attributes cannot be empty");
                    return null;
                }
                return new Park(name, country);
            }
            return null;
        });

        dialog.showAndWait().ifPresent(park -> {
            parkRepository.update(selected.getId(), park);
            loadParks();
        });
    }

    @FXML private void onDeletePark() {
        Park selected = parksTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("No park selected!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete " + selected.getName());
        alert.setContentText("Are you sure you want to delete " + selected.getName() + " " + selected.getCounty());

        alert.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                parkRepository.delete(selected.getId());
                loadParks();
            }
        });
    }

    @FXML private void onAddTrail() {}
    @FXML private void onEditTrail() {}
    @FXML private void onDeleteTrail() {}

    @FXML private void onAddTag() {}
    @FXML private void onEditTag() {}
    @FXML private void onDeleteTag() {}

    private void showError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
