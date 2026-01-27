package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.example.domain.*;
import org.example.service.BookService;
import org.example.service.BorrowService;

import java.util.ArrayList;
import java.util.List;


public class PatronViewController implements Observer {
    private BookService bookService;
    private BorrowService borrowService;
    private Patron patron;

    private List<Book> orderList = new ArrayList<>();

    @FXML private VBox table;
    @FXML private TableView<Book> tableBView;
    @FXML private TableColumn<Book, String> titleBColumn;
    @FXML private TableColumn<Book, String> authorBColumn;

    @FXML private TableView<BorrowRequest> borrowed;
    @FXML private TableColumn<BorrowRequest, String> patronColumn;
    @FXML private TableColumn<BorrowRequest, String> bookColumn;
    @FXML private TableColumn<BorrowRequest, Void> action2Column;

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
        setUpTables();
    }

    public void setPatron(Patron patron) {
        this.patron = patron;
        setUpTables();
    }

    public void setBorrowService(BorrowService borrowService) {
        this.borrowService = borrowService;
        borrowService.add(this);
        loadBorrowed();
    }

    private void setUpTables(){
        if(bookService == null) return;
        if(patron == null) return;

        for(String genre: bookService.getGenres()){
            TableView<Book> tableView = new TableView<>();

            TableColumn<Book, Integer> idColumn = new TableColumn<>();
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

            TableColumn<Book, String> titleColumn = new TableColumn<>();
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

            TableColumn<Book, Integer> authorColumn = new TableColumn<>();
            authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

            TableColumn<Book, Void> actionColumn = new TableColumn<>();

            actionColumn.setCellFactory(col -> new TableCell<Book, Void>() {
                private final Button btn = new Button("Borrow");

                {
                    btn.setOnAction(e -> {
                        Book book = getTableView().getItems().get(getIndex());
                        if(!orderList.contains(book)){
                            orderList.add(book);
                            loadTableB();
                        }
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

            tableView.getColumns().addAll(idColumn, titleColumn, authorColumn, actionColumn);

            ObservableList<Book> data = FXCollections.observableList(bookService.getFiltered(genre));
            tableView.setItems(data);

            Label label = new Label(genre);

            table.getChildren().addAll(label, tableView);
        }
    }

    private void loadTableB(){
        titleBColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorBColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        ObservableList<Book> data = FXCollections.observableList(orderList);
        tableBView.setItems(data);
    }

    public void loadBorrowed(){
        if(patron == null) return;
        if(borrowService == null) return;

        patronColumn.setCellValueFactory(new PropertyValueFactory<>("patron"));
        bookColumn.setCellValueFactory(new PropertyValueFactory<>("books"));
        action2Column.setCellFactory(col -> new TableCell<BorrowRequest, Void>() {
            private final Button btn = new Button("Return");

            {
                btn.setOnAction(e -> {
                    BorrowRequest req = getTableView().getItems().get(getIndex());

                    borrowService.updateStatus(req.getId(), Status.RETURNED);
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

        ObservableList<BorrowRequest> data = FXCollections.observableList(borrowService.getAll(patron.getId()));
        borrowed.setItems(data);
    }

    @FXML private void handleOrder(){
        if(orderList.isEmpty()) return;

        borrowService.add(patron, orderList);
        orderList.clear();
        loadTableB();
    }

    @Override
    public void update() {
        loadBorrowed();
    }
}
