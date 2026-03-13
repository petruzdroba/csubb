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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @FXML private TableColumn<Trail, String> trailTagsCol;

    private final ObservableList<Park> parkList = FXCollections.observableArrayList();
    private final ObservableList<Trail> trailList = FXCollections.observableArrayList();
    private final ObservableList<Tag> tagList = FXCollections.observableArrayList();

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
        trailTagsCol.setCellValueFactory(cellData -> {
            List<Tag> tags = cellData.getValue().getTags();
            String tagNames = tags.stream()
                    .map(Tag::getName)
                    .collect(Collectors.joining(", "));
            return new javafx.beans.property.SimpleStringProperty(tagNames);
        });

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
                Park park = new Park(name, country);
                park.setId(selected.getId());
                return park;
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

    @FXML private void onAddTrail() {
        Park selectedPark = parksTable.getSelectionModel().getSelectedItem();
        if (selectedPark == null) {
            showError("No park selected!");
            return;
        }

        Dialog<Trail> dialog = new Dialog<>();
        dialog.setTitle("Add Trail to " + selectedPark.getName());

        GridPane grid = new GridPane();

        TextField nameField = new TextField();
        TextField lengthField = new TextField();
        ListView<Tag> tagListView = new ListView<>(tagList);
        tagListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tagListView.setPrefHeight(100);

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Length:"), 0, 1);
        grid.add(lengthField, 1, 1);
        grid.add(new Label("Tags:"), 0, 2);
        grid.add(tagListView, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                String name = nameField.getText().trim();
                String lengthText = lengthField.getText().trim();
                if (name.isEmpty() || lengthText.isEmpty()) {
                    showError("Trail attributes cannot be empty!");
                    return null;
                }
                try {
                    double length = Double.parseDouble(lengthText);
                    List<Tag> selectedTags = new ArrayList<>(tagListView.getSelectionModel().getSelectedItems());
                    return new Trail(name, length, selectedPark, selectedTags);
                } catch (NumberFormatException e) {
                    showError("Length must be a number!");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(trail -> {
            trailRepository.add(trail);
            onParkSelected(selectedPark);
        });
    }

    @FXML private void onEditTrail() {
        Trail selected = trailsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("No trail selected!");
            return;
        }

        Dialog<Trail> dialog = new Dialog<>();
        dialog.setTitle("Edit Trail");

        GridPane grid = new GridPane();

        TextField nameField = new TextField(selected.getName());
        TextField lengthField = new TextField(String.valueOf(selected.getLength()));
        ListView<Tag> tagListView = new ListView<>(tagList);
        tagListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tagListView.setPrefHeight(100);

        // preslect existing tags
        for (Tag tag : selected.getTags()) {
            tagListView.getSelectionModel().select(tag);
        }

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Length:"), 0, 1);
        grid.add(lengthField, 1, 1);
        grid.add(new Label("Tags:"), 0, 2);
        grid.add(tagListView, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                String name = nameField.getText().trim();
                String lengthText = lengthField.getText().trim();
                if (name.isEmpty() || lengthText.isEmpty()) {
                    showError("Trail attributes cannot be empty!");
                    return null;
                }
                try {
                    double length = Double.parseDouble(lengthText);
                    List<Tag> selectedTags = new ArrayList<>(tagListView.getSelectionModel().getSelectedItems());
                    Trail trail = new Trail(name, length, selected.getPark(), selectedTags);
                    trail.setId(selected.getId());
                    return trail;
                } catch (NumberFormatException e) {
                    showError("Length must be a number!");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(trail -> {
            trailRepository.update(selected.getId(), trail);
            onParkSelected(selected.getPark());
        });
    }

    @FXML private void onDeleteTrail() {
        Trail selected = trailsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("No trail selected!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete " + selected.getName());
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete " + selected.getName() + "?");

        alert.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                trailRepository.delete(selected.getId());
                onParkSelected(selected.getPark());
            }
        });
    }

    @FXML private void onAddTag() {
        Dialog<Tag> dialog = new Dialog<>();
        dialog.setTitle("Add Tag");

        GridPane grid = new GridPane();

        TextField nameField = new TextField();
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                String name = nameField.getText().trim();
                if (name.isEmpty()) {
                    showError("Tag name cannot be empty!");
                    return null;
                }
                return new Tag(name);
            }
            return null;
        });

        dialog.showAndWait().ifPresent(tag -> {
            tagRepository.add(tag);
            loadTags();
        });
    }

    @FXML private void onEditTag() {
        Tag selected = tagsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("No tag selected!");
            return;
        }

        Dialog<Tag> dialog = new Dialog<>();
        dialog.setTitle("Edit Tag");

        GridPane grid = new GridPane();

        TextField nameField = new TextField(selected.getName());
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                String name = nameField.getText().trim();
                if (name.isEmpty()) {
                    showError("Tag name cannot be empty!");
                    return null;
                }
                Tag tag = new Tag(name);
                tag.setId(selected.getId());
                return tag;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(tag -> {
            tagRepository.update(selected.getId(), tag);
            loadTags();
        });
    }

    @FXML private void onDeleteTag() {
        Tag selected = tagsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("No tag selected!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete " + selected.getName());
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete " + selected.getName() + "?");

        alert.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                tagRepository.delete(selected.getId());
                loadTags();
            }
        });
    }

    private void showError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
