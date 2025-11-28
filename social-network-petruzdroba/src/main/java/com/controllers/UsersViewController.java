package com.controllers;

import com.domain.Duck;
import com.service.UserService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UsersViewController {
    private UserService userService;

    public UsersViewController() {
    }

    public void loadData(List<Duck> ducks) {
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
    public void initialize() {
        comboBox.setItems(FXCollections.observableArrayList("ALL", "FLYING", "SWIMMING", "FLYING_AND_SWIMMING"
        ));
        comboBox.setValue("ALL");

        comboBox.setOnAction(event -> {
            String selected = comboBox.getValue();
            if (selected.equals("ALL")) {
                loadData(new ArrayList<>(userService.getAllDucks()));
            } else
                try {
                    loadData(userService.getDucksByType(Duck.TipRata.valueOf(selected)));
                }
                catch (RuntimeException e){
                    System.out.println(e.getMessage());
                    Platform.exit();
                }
        });
    }


    public void setUserService(UserService userService) {
        this.userService = userService;
        loadData(new ArrayList<>(userService.getAllDucks()));
    }

    @FXML
    private void onExitButtonClick() {
        Platform.exit();
    }
}
