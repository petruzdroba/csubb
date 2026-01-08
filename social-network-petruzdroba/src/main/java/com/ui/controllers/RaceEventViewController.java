package com.ui.controllers;

import com.domain.*;
import com.service.RaceEventService;
import com.service.UserService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.Collection;

public class RaceEventViewController implements Observer {

    private RaceEventService raceEventService;
    private UserService userService;
    private User loggedInUser;

    private int pageCount = 1;
    private final int pageSize = 10;

    @FXML private VBox laneForm;

    @FXML private TableView<RaceEvent> tableView;
    @FXML private TableColumn<RaceEvent, String> ownerEmailColumn;
    @FXML private TableColumn<RaceEvent, String> laneDistancesColumn;
    @FXML private TableColumn<RaceEvent, String> participantsColumn;
    @FXML private TableColumn<RaceEvent, Void> deleteColumn;
    @FXML private TableColumn<RaceEvent, Void> startRaceColumn;
    @FXML private TableColumn<RaceEvent, Void> subscribeColumn;


    @FXML private TextField laneDistanceField;
    @FXML private Button addLaneButton;
    @FXML private Button createEventButton;

    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Label pageLabel;


    private final Collection<Culoar> lanes = FXCollections.observableArrayList();

    public void setRaceEventService(RaceEventService raceEventService) {
        this.raceEventService = raceEventService;
        tryLoadPage();
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
        tryLoadPage();
    }


    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        loggedInUser.addObserver(this);
        raceEventService.addObserver(loggedInUser);
        tryLoadPage();
    }

    @FXML
    private void initialize() {
        prevButton.setOnAction(e -> onPrevPage());
        nextButton.setOnAction(e -> onNextPage());

        ownerEmailColumn.setCellValueFactory(cellData -> {
            RaceEvent event = cellData.getValue();
            if (userService == null) {
                return new javafx.beans.property.SimpleStringProperty("");
            }
            try {
                User owner = userService.find(event.getOwnerId());
                return new javafx.beans.property.SimpleStringProperty(
                        owner != null ? owner.getEmail() : "Unknown"
                );
            } catch (SQLException e) {
                return new javafx.beans.property.SimpleStringProperty("Error");
            }
        });

        participantsColumn.setCellValueFactory(cellData -> {
            RaceEvent event = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(
                    event.getContainer().getDucks().stream()
                            .map(User::getEmail)
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("None")
            );
        });

        laneDistancesColumn.setCellValueFactory(cellData -> {
            RaceEvent event = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(
                    event.getContainer().getCuloare().stream()
                            .map(c -> String.valueOf(c.getDistanta()))
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("")
            );
        });

        setupActionColumns();
        tryLoadPage();
    }

    @FXML
    private void addLane() {
        try {
            int distance = Integer.parseInt(laneDistanceField.getText().trim());
            lanes.add(new Culoar(distance, lanes.size()));
            laneDistanceField.clear();
        } catch (NumberFormatException e) {
            showError("Distance must be a number");
        }
    }

    @FXML
    private void createEvent() {
        try {
            raceEventService.add(loggedInUser, lanes);
            lanes.clear();
            laneDistanceField.clear();
            loadCurrentPage();
        } catch (SQLException | RuntimeException e) {
            e.printStackTrace();
            showError(e.getMessage());
        }
    }

    private void setupActionColumns() {

        deleteColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteBtn = new Button("Delete");

            {
                deleteBtn.setOnAction(e -> {
                    RaceEvent event = getTableView().getItems().get(getIndex());
                    try {
                        raceEventService.remove(loggedInUser, event.getId());
                        loadCurrentPage();
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
                    return;
                }

                RaceEvent event = getTableView().getItems().get(getIndex());
                deleteBtn.setDisable(event.getOwnerId() != loggedInUser.getId());
                setGraphic(deleteBtn);
            }
        });

        startRaceColumn.setCellFactory(col -> new TableCell<>() {
            private final Button startBtn = new Button("Start");

            {
                startBtn.setOnAction(e -> {
                    RaceEvent event = getTableView().getItems().get(getIndex());
                    try {
                        raceEventService.startRace(loggedInUser, event.getId());
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
                    return;
                }

                RaceEvent event = getTableView().getItems().get(getIndex());
                startBtn.setDisable(event.getOwnerId() != loggedInUser.getId());
                setGraphic(startBtn);
            }
        });

        subscribeColumn.setCellFactory(col -> new TableCell<>() {
            private final Button subscribeBtn = new Button();

            {
                subscribeBtn.setOnAction(e -> {
                    RaceEvent event = getTableView().getItems().get(getIndex());
                    handleSubscribe(event);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }

                RaceEvent event = getTableView().getItems().get(getIndex());

                boolean isSubscribed = event.getSubscribers().stream()
                        .anyMatch(user -> user.getId() == loggedInUser.getId());

                subscribeBtn.setText(isSubscribed ? "Unsubscribe" : "Subscribe");
                setGraphic(subscribeBtn);
            }
        });
    }

    @FXML
    private void handleSubscribe(RaceEvent raceEvent){
        boolean isSubscribed = raceEvent.getSubscribers().stream()
                .anyMatch(user -> user.getId() == loggedInUser.getId());

        try{
            if(isSubscribed){
                raceEventService.unsubscribe(loggedInUser, raceEvent.getId());
            } else{
                raceEventService.subscribe(loggedInUser, raceEvent.getId());
            }
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    private void updatePageButtons() {
        prevButton.setDisable(pageCount <= 1);
        int totalPages = raceEventService.pageCount(pageSize);
        nextButton.setDisable(pageCount >= totalPages);
    }

    private void onPrevPage() {
        if (pageCount > 1) {
            pageCount--;
            loadCurrentPage();
        }
    }

    private void onNextPage() {
        int totalPages = raceEventService.pageCount(pageSize);
        if (pageCount < totalPages) {
            pageCount++;
            loadCurrentPage();
        }
    }

    private void tryLoadPage() {
        if (raceEventService != null
                && userService != null
                && loggedInUser != null) {
            loadCurrentPage();
        }
    }


    private void loadCurrentPage() {
        if (raceEventService == null) return;

        int offset = (pageCount - 1) * pageSize;
        try {
            Collection<RaceEvent> events =
                    raceEventService.getPage(offset, pageSize);

            tableView.setItems(FXCollections.observableArrayList(events));
            pageLabel.setText("Page: " + pageCount);
            updatePageButtons();
        } catch (RuntimeException e) {
            showError(e.getMessage());
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
        setupActionColumns();
    }

    @Override
    public void update(String message) {
        loadCurrentPage();
        setupActionColumns();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Race ended");
        alert.setHeaderText(loggedInUser.getEmail() + " Race Notification");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
