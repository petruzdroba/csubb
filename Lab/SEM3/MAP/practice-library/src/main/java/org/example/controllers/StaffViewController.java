package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.domain.BorrowRequest;
import org.example.domain.Observer;
import org.example.domain.Status;
import org.example.service.BorrowService;

public class StaffViewController implements Observer {
    private BorrowService borrowService;


    @FXML private TableView<BorrowRequest> tableView;
    @FXML private TableColumn<BorrowRequest, String> patronColumn;
    @FXML private TableColumn<BorrowRequest, String> bookColumn;
    @FXML private TableColumn<BorrowRequest, Void> actionColumn;


    public void setBorrowService(BorrowService borrowService) {
        this.borrowService = borrowService;
        borrowService.add(this);
        loadData();
    }

    public void loadData(){
        patronColumn.setCellValueFactory(new PropertyValueFactory<>("patron"));
        bookColumn.setCellValueFactory(new PropertyValueFactory<>("books"));
        actionColumn.setCellFactory(col -> new TableCell<BorrowRequest, Void>() {
            private final Button btn = new Button("Give");

            {
                btn.setOnAction(e -> {
                    BorrowRequest req = getTableView().getItems().get(getIndex());

                    borrowService.updateStatus(req.getId(), Status.BORROWED);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });

        ObservableList<BorrowRequest> data = FXCollections.observableList(borrowService.getAll());
        tableView.setItems(data);
    }

    @Override
    public void update() {
        loadData();
    }
}
