package com.ui.controllers;

import com.domain.Observer;
import com.domain.User;
import com.service.FriendshipService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FriendshipPagedViewByUserController implements Observer {

    private FriendshipService friendshipService;
    private User filterUser;
    private int pageCount = 1;
    private final int pageSize = 10;

    @FXML private TableView<User> tableView;
    @FXML private TableColumn<User, Long> idColumn;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, Void> actionColumn;

    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Label pageLabel;

    public void setFriendshipService(FriendshipService service, User user) {
        this.friendshipService = service;
        this.filterUser = user;
        service.addObserver(this);
        loadCurrentPage();
    }

    @FXML
    public void initialize() {
        prevButton.setOnAction(e -> onPrevPage());
        nextButton.setOnAction(e -> onNextPage());
        setupTableColumns();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button removeButton = new Button("Remove");

            {
                removeButton.setOnAction(e -> {
                    User otherUser = getTableView().getItems().get(getIndex());
                    onRemoveFriendship(otherUser);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : removeButton);
            }
        });
    }

    public void loadData(List<Map.Entry<User, User>> friendships) {
        List<User> otherUsers = new ArrayList<>();
        for (Map.Entry<User, User> entry : friendships) {
            if (entry.getKey().equals(filterUser)) {
                otherUsers.add(entry.getValue());
            } else {
                otherUsers.add(entry.getKey());
            }
        }

        ObservableList<User> data = FXCollections.observableList(otherUsers);
        tableView.setItems(data);
        pageLabel.setText("Page: " + pageCount);
        updatePageButtons();
    }

    public void loadCurrentPage() {
        if (friendshipService == null || filterUser == null) return;

        int offset = (pageCount - 1) * pageSize;
        try {
            List<Map.Entry<User, User>> page = new ArrayList<>(friendshipService.getAllPrettyByUser(filterUser, offset, pageSize));
            loadData(page);
        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

    private void updatePageButtons() {
        prevButton.setDisable(pageCount <= 1);
        int totalPages = friendshipService.pageCountByUser(filterUser, pageSize);
        nextButton.setDisable(pageCount >= totalPages);
    }

    public void onPrevPage() {
        if (pageCount > 1) {
            pageCount--;
            loadCurrentPage();
        }
    }

    public void onNextPage() {
        int totalPages = friendshipService.pageCountByUser(filterUser, pageSize);
        if (pageCount < totalPages) {
            pageCount++;
            loadCurrentPage();
        }
    }

    private void onRemoveFriendship(User otherUser) {
        // implement removal logic here
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void update() {
        loadCurrentPage();
    }
}
