package org.example.controllers;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import org.example.domain.*;
import org.example.service.OrderServiceI;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DriverViewController implements Observer, Observable {
    private OrderServiceI orderService;
    private Driver driver;
    private int pageCount = 1;
    private final int pageSize = 5;

    List<Observer> observers = new ArrayList<>();

    @FXML
    private TableView<Order> tableView;
    @FXML
    private TableColumn<Order, Integer> idColumn;
    @FXML
    private TableColumn<Order, String> startDateColumn;
    @FXML
    private TableColumn<Order, String> endDateColumn;
    @FXML
    private TableColumn<Order, String> statusColumn;
    @FXML
    private TableColumn<Order, String> pickupColumn;
    @FXML
    private TableColumn<Order, String> destinationColumn;
    @FXML
    private TableColumn<Order, String> clientColumn;
    @FXML
    private TableColumn<Order, Void> actionColumn;


    @FXML
    private Button prevButton;
    @FXML
    private Button nextButton;
    @FXML
    private Label pageLabel;


    public void setOrderService(OrderServiceI orderService) {
        this.orderService = orderService;
        orderService.addO(this);
        addO(orderService);
        loadCurrentPage();
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
        loadCurrentPage();
    }

    public void loadData(List<Order> orders) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        pickupColumn.setCellValueFactory(new PropertyValueFactory<>("pickupAddress"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destinationAddress"));
        clientColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));

        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button finishBtn = new Button("Finished");

            {
                finishBtn.setOnAction(e -> {
                    Order otherOrder = getTableView().getItems().get(getIndex());
                    orderService.markFinished(otherOrder.getId());
                    loadCurrentPage();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(finishBtn);
                }
            }
        });

        ObservableList<Order> data = FXCollections.observableList(orders);
        tableView.setItems(data);
    }

    private void loadCurrentPage() {
        if (orderService == null) return;
        if (driver == null) return;

        int offset = (pageCount - 1) * pageSize;

        try {
            loadData(orderService.getPage(pageSize, offset, Status.IN_PROGRESS, driver.getId()));

            pageLabel.setText("Page: " + pageCount);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            Platform.exit();
        }
    }

    private void updatePageButtons() {
        prevButton.setDisable(pageCount <= 1);
        int totalPages = orderService.pageCount(pageSize, Status.IN_PROGRESS, driver.getId());
        nextButton.setDisable(pageCount >= totalPages);
    }

    public void onPrevPage() {
        if (pageCount > 1) {
            pageCount--;
            loadCurrentPage();
        }
    }

    public void onNextPage() {
        int totalPages = orderService.pageCount(pageSize, Status.IN_PROGRESS, driver.getId());
        if (pageCount < totalPages) {
            pageCount++;
            loadCurrentPage();
        }
    }

    private boolean showConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm "+driver.getName());
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(yesButton, noButton);

        PauseTransition timeout = new PauseTransition(Duration.seconds(5));
        timeout.setOnFinished(e -> {
            if (alert.isShowing()) {
                alert.close();
            }
        });
        timeout.play();

        Optional<ButtonType> result = alert.showAndWait();
        timeout.stop();

        return result.isPresent() && result.get() == yesButton;
    }


    @Override
    public void update(Order order) {
        if(order == null){
            loadCurrentPage();
            return;
        };

        Platform.runLater(() -> {
            loadCurrentPage();

            boolean accepted = showConfirmation("Want order " + order.getId());

            if(accepted) {
                orderService.update(order.getId(), driver.getId());
            } else {
                orderService.notifyO(order);
            }
        });
    }

    @Override
    public void notifyO(Order order) {
        observers.forEach(o -> o.update(order));
    }

    @Override
    public void addO(Observer o) {
        if(!observers.contains(o))
            observers.add(o);
    }

    @Override
    public void removeO(Observer o) {
        observers.remove(o);
    }
}
