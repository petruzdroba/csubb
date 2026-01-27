package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.example.domain.MenuItem;
import org.example.domain.Table;
import org.example.service.MenuService;
import org.example.service.OrderService;

import java.util.ArrayList;
import java.util.List;

public class CustomerViewController {
    private MenuService menuService;
    private OrderService orderService;
    private Table table;

    private List<MenuItem> orderList = new ArrayList<>();
    @FXML
    private TableView<MenuItem> orderTable;
    @FXML
    private TableColumn<MenuItem, String> itemColumnOrder;
    @FXML
    private TableColumn<MenuItem, Float> priceColumnOrder;
    @FXML
    private TableColumn<MenuItem, String> currencyColumnOrder;

    @FXML
    private VBox tableContainer;

    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
        loadServiceData();
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public void setTable(Table table) {
        this.table = table;
        loadServiceData();
    }

    private void loadOrderTable(List<MenuItem> data) {
        itemColumnOrder.setCellValueFactory(new PropertyValueFactory<>("item"));
        priceColumnOrder.setCellValueFactory(new PropertyValueFactory<>("price"));
        currencyColumnOrder.setCellValueFactory(new PropertyValueFactory<>("currency"));

        ObservableList<MenuItem> items = FXCollections.observableList(data);
        orderTable.setItems(items);
    }

    @FXML
    private void initialize() {
        orderTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldItem, selectedItem) -> {
                    if (selectedItem == null) return;

                    if (orderList.contains(selectedItem)) {
                        orderList.remove(selectedItem);
                        orderTable.refresh();
                    }
                });
    }

    private void loadServiceData() {
        if (menuService == null) return;
        if (table == null) return;

        for (String category : menuService.getCategories()) {
            TableView<MenuItem> tableView = new TableView<>();

            TableColumn<MenuItem, Integer> idColumn = new TableColumn<>();
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

            TableColumn<MenuItem, String> itemColumn = new TableColumn<>();
            itemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));

            TableColumn<MenuItem, Float> priceColumn = new TableColumn<>();
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

            TableColumn<MenuItem, String> currencyColumn = new TableColumn<>();
            currencyColumn.setCellValueFactory(new PropertyValueFactory<>("currency"));

            //onselect
            tableView.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldItem, selectedItem) -> {
                        if (!orderList.contains(selectedItem)) {
                            orderList.add(selectedItem);
                            loadOrderTable(orderList);
                        }
                    });

            tableView.getColumns().addAll(idColumn, itemColumn, priceColumn, currencyColumn);


            List<MenuItem> items = menuService.getByCategory(category);

            tableView.setItems(FXCollections.observableList(items));

            Label label = new Label(category);
            tableContainer.getChildren().addAll(label, tableView);
        }
    }

    @FXML
    private void handleOrder(){
        if(orderList.isEmpty()) return;

        orderService.add(table.getId(), orderList);
        orderList.clear();
        loadOrderTable(orderList);
    }
}
