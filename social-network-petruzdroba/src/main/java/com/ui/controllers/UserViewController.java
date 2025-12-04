package com.ui.controllers;

import com.domain.Duck;
import com.domain.Observer;
import com.domain.Persoana;
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

public class UserViewController extends AbstractViewController<Long, User> implements Observer {

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
        this.service = userService;
        userService.addObserver(this);
        loadCurrentPage();
        updatePageButtons();
    }

    @FXML
    public void initialize() {
        typeSelector.setItems(FXCollections.observableArrayList("Persoana", "Duck"));
        typeSelector.setValue("Persoana");
        typeSelector.setOnAction(event -> updateFormVisibility());
        updateFormVisibility();

        duckType.setItems(FXCollections.observableArrayList("FLYING", "SWIMMING", "FLYING_AND_SWIMMING"));
        duckType.setValue("FLYING");

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selectedUser) -> {
            if (selectedUser != null) {
                idField.setText(String.valueOf(selectedUser.getId()));
                usernameField.setText(selectedUser.getUsername());
                emailField.setText(selectedUser.getEmail());
                passwordField.setText(selectedUser.getPassword());

                if (selectedUser instanceof Duck duck) {
                    typeSelector.setValue("Duck");
                    updateFormVisibility();
                    duckViteza.setText(String.valueOf(duck.getViteza()));
                    duckRezistenta.setText(String.valueOf(duck.getRezistenta()));
                    duckType.setValue(duck.getTip().name());
                } else {
                    typeSelector.setValue("Persoana");
                    updateFormVisibility();
                    Persoana persoana = (Persoana) selectedUser;
                    persoanaNume.setText(persoana.getNume());
                    persoanaPrenume.setText(persoana.getPrenume());
                    persoanaOcupatie.setText(persoana.getOcupatie());
                    persoanaEmpatie.setText(String.valueOf(persoana.getNivelEmpatie()));
                    persoanaDataNasterii.setValue(persoana.getDataNasterii());
                }
            }
        });
    }

    private void updateFormVisibility() {
        boolean isPersoana = "Persoana".equals(typeSelector.getValue());
        persoanaForm.setVisible(isPersoana);
        persoanaForm.setManaged(isPersoana);

        boolean isDuck = "Duck".equals(typeSelector.getValue());
        duckForm.setVisible(isDuck);
        duckForm.setManaged(isDuck);
    }

    @FXML
    public void addPersoana() {
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

            ((UserService) service).add(id, username, email, password, nume, prenume, dataNasterii, ocupatie, nivelEmpatie);
            clearForm();
        } catch (NumberFormatException e) {
            showError("ID and Empatie must be numbers");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    public void addDuck() {
        try {
            long id = Long.parseLong(idField.getText().trim());
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            double viteza = Double.parseDouble(duckViteza.getText().trim());
            double rezistenta = Double.parseDouble(duckRezistenta.getText().trim());
            String type = duckType.getValue();

            ((UserService) service).add(id, username, email, password, Duck.TipRata.valueOf(type), viteza, rezistenta);
            clearForm();
        } catch (NumberFormatException e) {
            showError("ID, viteza, and rezistenta must be numbers");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
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

    @FXML
    public void deleteUser() {
        try {
            long id = Long.parseLong(idField.getText().trim());
            ((UserService) service).remove(id);
            clearForm();
        } catch (NumberFormatException e) {
            showError("ID must be numbers");
        } catch (Exception e) {
            showError(e.getMessage());
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

    @Override
    public void loadCurrentPage() {
        if (service == null) return;
        int offset = (pageCount - 1) * pageSize;
        loadData(new ArrayList<>(service.getPage(offset, pageSize)));
        pageLabel.setText("Page: " + pageCount);
        updatePageButtons();
    }

    @Override
    public void update() {
        loadCurrentPage();
    }
}
