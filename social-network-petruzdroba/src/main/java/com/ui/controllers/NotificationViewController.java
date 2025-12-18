package com.ui.controllers;

import com.domain.Notification;
import com.domain.Observer;
import com.domain.User;
import com.exceptions.ValidationException;
import com.service.NotificationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NotificationViewController implements Observer {

    private NotificationService notificationService;
    private User loggedUser;

    private int pageCount = 1;
    private final int pageSize = 10;
    private boolean showingUnread = false;

    @FXML private TabPane tabPane;
    @FXML private Tab allTab;
    @FXML private Tab unreadTab;

    @FXML private TableView<Notification> tableView;
    @FXML private TableColumn<Notification, String> textColumn;
    @FXML private TableColumn<Notification, String> timestampColumn;
    @FXML private TableColumn<Notification, Void> actionColumn;

    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Label pageLabel;

    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void setLoggedInUser(User loggedUser) {
        this.loggedUser = loggedUser;

        loggedUser.addObserver(this);
        notificationService.addObserver(loggedUser);

        loadCurrentPage();
    }

    @FXML
    public void initialize() {
        prevButton.setOnAction(e -> onPrevPage());
        nextButton.setOnAction(e -> onNextPage());
        setupTableColumns();

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            showingUnread = newTab == unreadTab;
            pageCount = 1;
            loadCurrentPage();
        });
    }

    private void setupTableColumns() {
        textColumn.setCellValueFactory(new PropertyValueFactory<>("text"));
        timestampColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getData().format(DateTimeFormatter.ofPattern("HH:mm"))
                )
        );

        actionColumn.setCellFactory(col -> new TableCell<Notification, Void>() {
            private final Button btn = new Button();

            {
                btn.setOnAction(e -> {
                    Notification notification = getTableView().getItems().get(getIndex());
                    try {
                        if (notification.isRead()) {
                            notificationService.markUnread(notification);
                        } else {
                            notificationService.markRead(notification);
                        }
                        getTableView().refresh();
                    } catch (Exception ex) {
                        showError(ex.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Notification notification = getTableView().getItems().get(getIndex());
                    btn.setText(notification.isRead() ? "Mark Unread" : "Mark Read");
                    setGraphic(btn);
                }
            }
        });
    }

    private void loadData(List<Notification> notifications) {
        ObservableList<Notification> data = FXCollections.observableList(notifications);
        tableView.setItems(data);
        pageLabel.setText("Page: " + pageCount);
        updatePageButtons();
    }

    public void loadCurrentPage() {
        if (notificationService == null || loggedUser == null) return;

        int offset = (pageCount - 1) * pageSize;
        try {
            List<Notification> page = new ArrayList<>();
            if (showingUnread) {
                page.addAll(notificationService.getUnreadNotificationsPage(loggedUser, offset, pageSize));
            } else {
                page.addAll(notificationService.getAllNotificationsPage(loggedUser, offset, pageSize));
            }
            loadData(page);
        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void markAllRead(){
        try{
            notificationService.markRead(new ArrayList<>(notificationService.getAllNotifications(loggedUser)));
        } catch (SQLException | ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    private void updatePageButtons() {
        prevButton.setDisable(pageCount <= 1);
        int totalPages = showingUnread ?
                notificationService.pageCountUnread(loggedUser, pageSize) :
                notificationService.pageCountAll(loggedUser, pageSize);
        nextButton.setDisable(pageCount >= totalPages);
    }

    public void onPrevPage() {
        if (pageCount > 1) {
            pageCount--;
            loadCurrentPage();
        }
    }

    public void onNextPage() {
        int totalPages = showingUnread ?
                notificationService.pageCountUnread(loggedUser, pageSize) :
                notificationService.pageCountAll(loggedUser, pageSize);
        if (pageCount < totalPages) {
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

    @Override
    public void update() {
        loadCurrentPage();
    }
}
