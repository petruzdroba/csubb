package org.zdroba.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

    @FXML
    public void initialize() {
        parkIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        parkNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

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

    @FXML private void onAddPark() {}
    @FXML private void onEditPark() {}
    @FXML private void onDeletePark() {}

    @FXML private void onAddTrail() {}
    @FXML private void onEditTrail() {}
    @FXML private void onDeleteTrail() {}

    @FXML private void onAddTag() {}
    @FXML private void onEditTag() {}
    @FXML private void onDeleteTag() {}
}
