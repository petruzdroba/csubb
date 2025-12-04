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
    private final int pageSize = 7;
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
            nextPage = new ArrayList<>(userService.getAllDucksPage(offset, pageSize));
        } else {
            nextPage = userService.getPaginatedDucksByType(Duck.TipRata.valueOf(selected), offset, pageSize);
        }
        if (!nextPage.isEmpty()) {
            pageCount++;
            loadData(nextPage);
            pageLabel.setText("Page: " + pageCount);
            updatePageButtons();
        }
    }

    private void updatePageButtons() {
        prevButton.setDisable(pageCount <= 1);
        int offset = pageCount * pageSize;
        String selected = comboBox.getValue();
        List<Duck> nextPage;

        if ("ALL".equals(selected)) {
            nextPage = new ArrayList<>(userService.getAllDucksPage(offset, pageSize));
        } else {
            nextPage = userService.getPaginatedDucksByType(Duck.TipRata.valueOf(selected), offset, pageSize);
        }

        nextButton.setDisable(nextPage.isEmpty());
    }


    @Override
    public void update() {
        loadCurrentPage();
    }
}