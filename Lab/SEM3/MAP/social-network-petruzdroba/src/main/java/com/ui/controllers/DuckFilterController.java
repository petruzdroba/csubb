package com.ui.controllers;

import com.domain.Duck;
import com.domain.Observer;
import com.service.UserService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class DuckFilterController implements Observer {
    private UserService userService;
    private int pageCount = 1;
    private final int pageSize = 5;
    @FXML
    private TableView<Duck> tableView;
    @FXML
    private TableColumn<Duck, Integer> idColumn;
    @FXML
    private TableColumn<Duck, String> usernameColumn;
    @FXML
    private TableColumn<Duck, String> emailColumn;
    @FXML
    private TableColumn<Duck, Integer> vitezaColumn;
    @FXML
    private TableColumn<Duck, Integer> rezistentaColumn;
    @FXML
    private TableColumn<Duck, Duck.TipRata> typeColumn;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private Button prevButton;
    @FXML
    private Button nextButton;
    @FXML
    private Label pageLabel;

    public DuckFilterController() {
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
        this.userService.addObserver(this);
        loadCurrentPage();
        updatePageButtons();
    }

    public void loadData(List<Duck> ducks) {
        double rowHeight = 26;
        tableView.setFixedCellSize(rowHeight);
        int rows = ducks.size();
        tableView.setPrefHeight(rows * rowHeight + 28);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        rezistentaColumn.setCellValueFactory(new PropertyValueFactory<>("rezistenta"));
        vitezaColumn.setCellValueFactory(new PropertyValueFactory<>("viteza"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("tip"));
        ObservableList<Duck> data = FXCollections.observableList(ducks);
        tableView.setItems(data);
    }

    @FXML
    public void initialize() {
        comboBox.setItems(FXCollections.observableArrayList("ALL", "FLYING", "SWIMMING", "FLYING_AND_SWIMMING"));
        comboBox.setValue("ALL");
        comboBox.setOnAction(event -> {
            pageCount = 1;
            loadCurrentPage();
            updatePageButtons();
        });
        loadCurrentPage();
    }

    private void loadCurrentPage() {
        if (userService == null) return;
        int offset = (pageCount - 1) * pageSize;
        String selected = comboBox.getValue();
        try {
            if (selected.equals("ALL")) {
                loadData(new ArrayList<Duck>(userService.getAllDucksPage(offset, pageSize)));
            } else {
                loadData(userService.getPaginatedDucksByType(Duck.TipRata.valueOf(selected), offset, pageSize));
            }
            pageLabel.setText("Page: " + pageCount);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            Platform.exit();
        }
    }

    @FXML
    private void onExitButtonClick() {
        Platform.exit();
    }

    public void onPrevPage() {
        if (pageCount > 1) {
            pageCount--;
            loadCurrentPage();
            updatePageButtons();
        }
    }

    public void onNextPage() {
        int offset = pageCount * pageSize;
        String selected = comboBox.getValue();
        List<Duck> nextPage;
        if (selected.equals("ALL")) {
            if(pageCount < userService.pageCount(pageSize)){
                pageCount++;
                loadCurrentPage();
            }
        } else {
            if(pageCount < userService.getPageDuck(pageSize, Duck.TipRata.valueOf(selected))){
                pageCount++;
                loadCurrentPage();
            }
        }
        updatePageButtons();
    }

    private void updatePageButtons() {
        prevButton.setDisable(pageCount <= 1);

        String selected = comboBox.getValue();
        if (selected.equals("ALL")) {
            nextButton.setDisable(pageCount >= userService.pageCount(pageSize));
        } else {
            nextButton.setDisable(pageCount >= userService.getPageDuck(pageSize, Duck.TipRata.valueOf(selected)));
        }
    }


    @Override
    public void update() {
        loadCurrentPage();
    }

    @Override
    public void update(String message) {
        loadCurrentPage();
    }
}