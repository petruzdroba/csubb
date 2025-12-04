package com.ui.controllers;

import com.domain.Duck;
import com.domain.Observer;
import com.domain.Persoana;
import com.domain.User;
import com.service.UserService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
    private final int pageSize = 5;

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
            showError("ID and Empatie must be numbers");
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

    private void loadCurrentPage() {
        if (userService == null) return;

        int offset = (pageCount - 1) * pageSize;

        try {
            loadData(new ArrayList<User>(userService.getPage(offset, pageSize)));
            pageLabel.setText("Page: " + pageCount);
        } catch (RuntimeException e) {
            showError(e.getMessage());
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

            double viteza = Double.parseDouble(duckViteza.getText().trim());
            double rezistenta = Double.parseDouble(duckRezistenta.getText().trim());

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

    private void updatePageButtons() {
        prevButton.setDisable(pageCount <= 1);
        nextButton.setDisable(pageCount >= userService.pageCount(pageSize));
    }

    public void onPrevPage() {
        if (pageCount > 1) {
            pageCount--;
            loadCurrentPage();
        }

        updatePageButtons();
    }

    public void onNextPage() {
        if (pageCount < userService.pageCount(pageSize)) {
            pageCount++;
            loadCurrentPage();
        }

        updatePageButtons();
    }

    public void deleteUser() {
        try{
            long id = Long.parseLong(idField.getText().trim());
            userService.remove(id);
            clearForm();
        }catch (NumberFormatException e) {
            showError("ID must be numbers");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void update() {
        loadCurrentPage();
    }
}
