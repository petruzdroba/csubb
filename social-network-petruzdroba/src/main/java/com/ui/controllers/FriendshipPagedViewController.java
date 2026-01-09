package com.ui.controllers;

import com.domain.Observer;
import com.domain.User;
import com.domain.Friendship;
import com.service.FriendshipService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FriendshipPagedViewController extends AbstractPagedViewController<String, Friendship> implements Observer {

    @FXML private TableView<Map.Entry<User, User>> tableView;
    @FXML private TableColumn<Map.Entry<User, User>, Long> id1Column;
    @FXML private TableColumn<Map.Entry<User, User>, String> username1Column;
    @FXML private TableColumn<Map.Entry<User, User>, Long> id2Column;
    @FXML private TableColumn<Map.Entry<User, User>, String> username2Column;

    @FXML private TextField id1Field;
    @FXML private TextField id2Field;

    public void setFriendshipService(FriendshipService friendshipService) {
        this.service = friendshipService;
        friendshipService.addObserver(this);
        loadCurrentPage();
    }

    @FXML
    public void initialize() {
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selectedEntry) -> {
            if (selectedEntry != null) {
                id1Field.setText(String.valueOf(selectedEntry.getKey().getId()));
                id2Field.setText(String.valueOf(selectedEntry.getValue().getId()));
            }
        });
    }

    public void loadData(List<Map.Entry<User, User>> friendships) {
        double rowHeight = 24;
        tableView.setFixedCellSize(rowHeight);
        tableView.setPrefHeight(5 * rowHeight + 28);

        id1Column.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleLongProperty(cell.getValue().getKey().getId()).asObject()
        );
        username1Column.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().getKey().getUsername())
        );
        id2Column.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleLongProperty(cell.getValue().getValue().getId()).asObject()
        );
        username2Column.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().getValue().getUsername())
        );

        ObservableList<Map.Entry<User, User>> data = FXCollections.observableList(friendships);
        tableView.setItems(data);
    }

    @Override
    public void loadCurrentPage() {
        if (service == null) return;

        int offset = (pageCount - 1) * pageSize;
        try {
            FriendshipService fs = (FriendshipService) service;
            List<Map.Entry<User, User>> page = new ArrayList<>(fs.getAllPretty(offset, pageSize));
            loadData(page);
            pageLabel.setText("Page: " + pageCount);
            updatePageButtons();
        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void communityNr() {
        FriendshipService fs = (FriendshipService) service;
        showMessage("Number of communities: " + fs.getCommunityCount());
    }

    @FXML
    private void mostSociable() {
        FriendshipService fs = (FriendshipService) service;
        showTablePopup(new ArrayList<>(fs.getMostSociableCommunity()));
    }

    private void showTablePopup(List<User> users) {
        TableView<User> popupTable = new TableView<>();

        TableColumn<User, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        popupTable.getColumns().addAll(idCol, usernameCol, emailCol);
        popupTable.setItems(FXCollections.observableList(users));

        VBox layout = new VBox(popupTable);
        layout.setPadding(new Insets(10));

        Stage popupStage = new Stage();
        popupStage.setTitle("Users");
        popupStage.setScene(new Scene(layout, 400, 300));
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.showAndWait();
    }

    public void addFriendship() {
        try {
            long id1 = Long.parseLong(id1Field.getText().trim());
            long id2 = Long.parseLong(id2Field.getText().trim());
            ((FriendshipService) service).add(id1, id2);
            clearForm();
        } catch (NumberFormatException e) {
            showError("IDs must be numbers");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    public void clearForm() {
        id1Field.clear();
        id2Field.clear();
    }

    public void deleteFriendship() {
        try {
            long id1 = Long.parseLong(id1Field.getText().trim());
            long id2 = Long.parseLong(id2Field.getText().trim());
            ((FriendshipService) service).remove(id1, id2);
            clearForm();
            if(pageCount > service.pageCount(pageSize)){
                pageCount--;
                loadCurrentPage();
            }
        } catch (NumberFormatException e) {
            showError("IDs must be numbers");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void update() {
        loadCurrentPage();
    }

    @Override
    public void update(String message) {
        loadCurrentPage();
    }
}
