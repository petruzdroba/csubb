package com.ui.controllers;

import com.domain.Duck;
import com.domain.Observer;
import com.domain.User;
import com.service.UserService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserViewController implements Observer {
    private UserService userService;
    private int pageCount = 1;
    private int pageSize = 5;

    @FXML
    private ComboBox<String> typeSelector;
    @FXML
    private VBox persoanaForm;
    @FXML
    private VBox duckForm;

    @FXML
    private TableView<User> tableView;
    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private Button prevButton;
    @FXML
    private Button nextButton;
    @FXML
    private Label pageLabel;

    @FXML
    private TextField idField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;

    @FXML
    private TextField persoanaNume;
    @FXML
    private TextField persoanaPrenume;
    @FXML
    private TextField persoanaOcupatie;
    @FXML
    private TextField persoanaEmpatie;
    @FXML
    private DatePicker persoanaDataNasterii;

    @FXML
    private TextField duckViteza;
    @FXML
    private TextField duckRezistenta;
    @FXML
    private ComboBox<String> duckType;

    public UserViewController() {
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
        this.userService.addObserver(this);
        loadCurrentPage();
    }

    @FXML
    public void initialize() {
        typeSelector.setItems(FXCollections.observableArrayList("Persoana", "Duck"));
        typeSelector.setValue("Persoana");

        typeSelector.setOnAction(event -> updateFormVisibility());

        updateFormVisibility();

        duckType.setItems(FXCollections.observableArrayList("FLYING", "SWIMMING", "FLYING_AND_SWIMMING"));
        duckType.setValue("FLYING");

        loadCurrentPage();
    }

    private void updateFormVisibility() {
        String selected = typeSelector.getValue();
        persoanaForm.setVisible("Persoana".equals(selected));
        persoanaForm.setManaged("Persoana".equals(selected));
        duckForm.setVisible("Duck".equals(selected));
        duckForm.setManaged("Duck".equals(selected));
    }

    @FXML
    private void addPersoana() {
        try {
            long id = Long.parseLong(idField.getText().trim());

            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            String nume = persoanaNume.getText().trim();
            String prenume = persoanaPrenume.getText().trim();
            String ocupatie = persoanaOcupatie.getText().trim();

            LocalDate dataNasterii = persoanaDataNasterii.getValue();

            int nivelEmpatie = Integer.parseInt(persoanaEmpatie.getText().trim());

            userService.add(id, username, email, password,
                    nume, prenume, dataNasterii, ocupatie, nivelEmpatie);

            clearForm();

        } catch (NumberFormatException e) {
            System.out.println("ID and Empatie must be numbers");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void loadData(List<User> users) {
        double rowHeight = 24;
        tableView.setFixedCellSize(rowHeight);
        tableView.setPrefHeight(5 * rowHeight + 28);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        ObservableList<User> data = FXCollections.observableList(users);

        tableView.setItems(data);
    }

    private void loadCurrentPage() {
        if (userService == null) return;

        int offset = (pageCount - 1) * pageSize;

        try {
            loadData(new ArrayList<User>(userService.getPage(offset, pageSize)));
            pageLabel.setText("Page: " + pageCount);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            Platform.exit();
        }
    }


    @FXML
    public void addDuck() {
        try {
            long id = Long.parseLong(idField.getText().trim());

            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();

            int viteza = Integer.parseInt(duckViteza.getText().trim());
            int rezistenta = Integer.parseInt(duckRezistenta.getText().trim());

            String type = duckType.getValue();

            userService.add(
                    id,
                    username,
                    email,
                    password,
                    Duck.TipRata.valueOf(type),
                    viteza,
                    rezistenta
            );

            clearForm();

        } catch (NumberFormatException e) {
            System.out.println("ID, viteza, and rezistenta must be numbers");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private void clearForm() {
        idField.clear();
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        persoanaNume.clear();
        persoanaPrenume.clear();
        persoanaOcupatie.clear();
        persoanaEmpatie.clear();
        persoanaDataNasterii.setValue(null);

        duckRezistenta.clear();
        duckViteza.clear();
        duckType.setValue("FLYING");
    }

    public void onPrevPage() {
        if (pageCount > 1) {
            pageCount--;
            loadCurrentPage();
        }
    }

    public void onNextPage() {
        if(pageCount < userService.pageCount(pageSize)){
            pageCount++;
            loadCurrentPage();
        }
    }

    @Override
    public void update() {
        loadCurrentPage();
    }
}
