package com.ui.controllers;

import com.domain.*;
import com.exceptions.ValidationException;
import com.service.RaceEventService;
import com.service.UserService;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.sql.SQLException;
import java.util.Collection;
import java.util.concurrent.CompletionException;

public class RaceEventViewController implements Observer {

    private RaceEventService raceEventService;
    private UserService userService;
    private User loggedInUser;

    private int pageCount = 1;
    private final int pageSize = 10;

    @FXML
    private VBox laneForm;

    @FXML
    private TableView<RaceEvent> tableView;
    @FXML
    private TableColumn<RaceEvent, String> ownerEmailColumn;
    @FXML
    private TableColumn<RaceEvent, String> laneDistancesColumn;
    @FXML
    private TableColumn<RaceEvent, String> participantsColumn;
    @FXML
    private TableColumn<RaceEvent, Void> deleteColumn;
    @FXML
    private TableColumn<RaceEvent, Void> startRaceColumn;
    @FXML
    private TableColumn<RaceEvent, Void> subscribeColumn;


    @FXML
    private TextField laneDistanceField;
    @FXML
    private Button addLaneButton;
    @FXML
    private Button createEventButton;
    @FXML
    private Button removeLastLaneButton;
    @FXML
    private Label lanesDisplayLabel;

    @FXML
    private Button prevButton;
    @FXML
    private Button nextButton;
    @FXML
    private Label pageLabel;


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
            if (distance < 1) {
                laneDistanceField.clear();
                throw new ValidationException("Lane distance must be > 1");
            }
            lanes.add(new Culoar(distance, lanes.size()));
            laneDistanceField.clear();
            updateLanesDisplay();
        } catch (NumberFormatException | ValidationException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void createEvent() {
        raceEventService.add(loggedInUser, lanes)
                .thenRun(() -> Platform.runLater(() -> {
                    lanes.clear();
                    laneDistanceField.clear();
                    updateLanesDisplay();
                    loadCurrentPage();
                }))
                .exceptionally(ex -> {
                    Platform.runLater(() -> showError(ex.getCause().getMessage()));
                    return null;
                });
    }

    private void setupActionColumns() {

        deleteColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteBtn = new Button("Delete");

            {
                deleteBtn.setOnAction(e -> {
                    RaceEvent event = getTableView().getItems().get(getIndex());
                    raceEventService.remove(loggedInUser, event.getId())
                            .thenRun(() -> Platform.runLater(() -> {
                                loadCurrentPage();
                            }))
                            .exceptionally(ex -> {
                                Platform.runLater(() -> showError(ex.getCause().getMessage()));
                                return null;
                            });
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
                    raceEventService.startRace(loggedInUser, event.getId())
                            .exceptionally(ex -> {
                                Platform.runLater(() -> showError(ex.getCause().getMessage()));
                                return null;
                            });
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
    private void handleSubscribe(RaceEvent raceEvent) {
        boolean isSubscribed = raceEvent.getSubscribers().stream()
                .anyMatch(user -> user.getId() == loggedInUser.getId());

        if (isSubscribed) {
            raceEventService.unsubscribe(loggedInUser, raceEvent.getId())
                    .thenRun(() -> Platform.runLater(this::loadCurrentPage))
                    .exceptionally(ex -> {
                        Platform.runLater(() -> showError(ex.getCause().getMessage()));
                        return null;
                    });
            ;
        } else {
            raceEventService.subscribe(loggedInUser, raceEvent.getId())
                    .thenRun(() -> Platform.runLater(this::loadCurrentPage))
                    .exceptionally(ex -> {
                        Platform.runLater(() -> showError(ex.getCause().getMessage()));
                        return null;
                    });
            ;
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

    @FXML
    private void removeLastLane() {
        if (!lanes.isEmpty()) {
            Culoar lastLane = null;
            for (Culoar lane : lanes) {
                lastLane = lane;
            }
            lanes.remove(lastLane);
            updateLanesDisplay();
        }
    }

    private void updateLanesDisplay() {
        if (lanes.isEmpty()) {
            lanesDisplayLabel.setText("No lanes added");
        } else {
            String lanesText = lanes.stream()
                    .map(c -> String.valueOf(c.getDistanta()))
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            lanesDisplayLabel.setText("Lanes: " + lanesText);
        }
    }


    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void removeObservers() {
        loggedInUser.removeObserver(this);
        raceEventService.removeObserver(loggedInUser);
    }

    @Override
    public void update() {
        loadCurrentPage();
        setupActionColumns();
    }

    private void showResult(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Race ended");
        alert.setHeaderText(loggedInUser.getEmail() + " Race Notification");
        alert.setContentText(message);

//        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
//        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
//
//        alert.getButtonTypes().setAll(yesButton, noButton);

        PauseTransition timeout = new PauseTransition(Duration.seconds(5));
        timeout.setOnFinished(e -> {
            if (alert.isShowing()) {
                alert.close();
            }
        });
        timeout.play();

        alert.showAndWait();
        timeout.stop();
    }

    @Override
    public void update(String message) {
        loadCurrentPage();
        setupActionColumns();

        showResult(message);
    }
}
