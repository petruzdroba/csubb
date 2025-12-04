package com.ui.controllers;

import com.domain.Observer;
import com.domain.User;
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

public class FriendshipViewController implements Observer {
    private FriendshipService friendshipService;
    private int pageCount = 1;
    private final int pageSize = 5;

    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
        friendshipService.addObserver(this);
        loadCurrentPage();
    }

    @FXML private TableView<Map.Entry<User, User>> tableView;
    @FXML private TableColumn<Map.Entry<User, User>, Long> id1Column;
    @FXML private TableColumn<Map.Entry<User, User>, String> username1Column;
    @FXML private TableColumn<Map.Entry<User, User>, Long> id2Column;
    @FXML private TableColumn<Map.Entry<User, User>, String> username2Column;

    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Label pageLabel;

    @FXML private TextField id1Field;
    @FXML private TextField id2Field;

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

    private void loadCurrentPage() {
        if (friendshipService == null) return;

        int offset = (pageCount - 1) * pageSize;
        try {
            loadData(new ArrayList<>(friendshipService.getAllPretty(offset, pageSize)));
            pageLabel.setText("Page: " + pageCount);
            updatePageButtons();
        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
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


    private void updatePageButtons() {
        prevButton.setDisable(pageCount <= 1);
        nextButton.setDisable(pageCount >= friendshipService.pageCount(pageSize));
    }

    public void onPrevPage() {
        if (pageCount > 1) {
            pageCount--;
            loadCurrentPage();
        }
    }

    public void onNextPage() {
        if (pageCount < friendshipService.pageCount(pageSize)) {
            pageCount++;
            loadCurrentPage();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void communityNr(){
        showMessage("Number of communities: " + friendshipService.getCommunityCount());
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
        popupStage.initModality(Modality.APPLICATION_MODAL); // Makes it modal
        popupStage.showAndWait();
    }

    @FXML
    private void mostSociable(){
        showTablePopup(new ArrayList<>(friendshipService.getMostSociableCommunity()));
    }

    public void addFriendship() {
        try {
            long id1 = Long.parseLong(id1Field.getText().trim());
            long id2 = Long.parseLong(id2Field.getText().trim());

            friendshipService.add(id1, id2);
            clearForm();
        } catch (NumberFormatException e) {
            showError("ID and Empatie must be numbers");
        }catch(Exception e){
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

            friendshipService.remove(id1, id2);
            clearForm();
        } catch (NumberFormatException e) {
            showError("ID and Empatie must be numbers");
        }catch(Exception e){
            showError(e.getMessage());
        }
    }

    @Override
    public void update() {
        loadCurrentPage();
    }
}
