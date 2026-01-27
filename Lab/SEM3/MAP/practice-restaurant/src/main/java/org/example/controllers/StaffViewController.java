package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.domain.Observer;
import org.example.domain.Order;
import org.example.service.OrderService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class StaffViewController implements Observer {
    private OrderService orderService;

    @FXML
    private TableView<Order> tableView;
    @FXML
    private TableColumn<Order, Integer> tableIdColumn;
    @FXML
    private TableColumn<Order, List<String>> itemsColumn;
    @FXML
    private TableColumn<Order, LocalDateTime> dateColumn;


    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
        orderService.add(this);
        loadCurrentPage();
    }

    public void loadData(List<Order> orders){
        tableIdColumn.setCellValueFactory(new PropertyValueFactory<>("tableId"));
        itemsColumn.setCellValueFactory(new PropertyValueFactory<>("menuItems"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        ObservableList<Order> data = FXCollections.observableList(orders);
        tableView.setItems(data);
    }

    private void loadCurrentPage(){
        if(orderService == null) return;

        loadData(orderService.getAll());
    }

    @Override
    public void update() {
        loadCurrentPage();
    }
}
