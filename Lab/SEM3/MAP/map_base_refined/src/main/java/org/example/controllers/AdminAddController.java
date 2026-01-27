package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.example.domain.Action;
import org.example.domain.Meci;
import org.example.domain.Team;
import org.example.service.EvenimentService;

import java.sql.SQLException;

public class AdminAddController {
    private Meci meci;
    private EvenimentService evenimentService;

    public void setEvenimentService(EvenimentService evenimentService) {
        this.evenimentService = evenimentService;
    }

    public void setMeci(Meci meci) {
        this.meci = meci;
    }

    @FXML
    private ComboBox<String> echipa;
    @FXML
    private ComboBox<String> action;
    @FXML
    private TextField rata;


    @FXML
    public void initialize() {
        echipa.setItems(FXCollections.observableArrayList("GAZDA", "OASPETE"));
        echipa.setValue("GAZDA");

        action.setItems(FXCollections.observableArrayList("GOL", "GALBEN", "ROSU"));
        action.setValue("GOL");
    }

    @FXML
    private void handleSubmit() throws SQLException {
        evenimentService.add(meci.getId(), Team.valueOf(echipa.getValue()), Integer.valueOf(rata.getText()), Action.valueOf(action.getValue()));
    }
}
