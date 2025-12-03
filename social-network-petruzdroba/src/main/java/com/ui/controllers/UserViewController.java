package com.ui.controllers;

import com.service.UserService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class UserViewController {
    private UserService userService;

    @FXML
    private ComboBox<String> typeSelector;
    @FXML private VBox persoanaForm;
    @FXML private VBox duckForm;

    @FXML private TextField idField;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField passwordField;

    @FXML private TextField persoanaNume;
    @FXML private TextField persoanaPrenume;
    @FXML private TextField persoanaOcupatie;
    @FXML private TextField persoanaEmpatie;
    @FXML private DatePicker persoanaDataNasterii;

    @FXML private TextField duckViteza;
    @FXML private TextField duckRezistenta;


    @FXML private ComboBox<String> duckType;

    public UserViewController() {
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @FXML
    public void initialize() {
        typeSelector.setItems(FXCollections.observableArrayList("Persoana", "Duck"));
        typeSelector.setValue("Persoana");

        typeSelector.setOnAction(event -> updateFormVisibility());

        updateFormVisibility();

        duckType.setItems(FXCollections.observableArrayList("FLYING", "SWIMMING", "FLYING_AND_SWIMMING"));
        duckType.setValue("FLYING");
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

            clearPersoanaForm();

        } catch (NumberFormatException e) {
            System.out.println("ID and Empatie must be numbers");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void clearPersoanaForm() {
        idField.clear();
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        persoanaNume.clear();
        persoanaPrenume.clear();
        persoanaOcupatie.clear();
        persoanaEmpatie.clear();
        persoanaDataNasterii.setValue(null);
    }

    public void addDuck() {
    }
}
