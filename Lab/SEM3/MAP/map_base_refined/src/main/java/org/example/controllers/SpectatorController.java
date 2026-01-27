package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import org.example.domain.Eveniment;
import org.example.domain.Meci;
import org.example.domain.Observer;
import org.example.domain.Team;
import org.example.service.EvenimentService;

public class SpectatorController implements Observer {
    private EvenimentService evenimentService;
    private Meci meci;

    @FXML
    private VBox container;

    public void setEvenimentService(EvenimentService evenimentService) {
        this.evenimentService = evenimentService;
        evenimentService.add(this);
        loadData();
    }

    public void setMeci(Meci meci) {
        this.meci = meci;
        loadData();
    }

    private void loadData() {
        if (meci == null) return;
        if (evenimentService == null) return;

        container.getChildren().add(new Label("             " + meci.toString()));
        for (Eveniment e : evenimentService.get(meci.getId())) {

            Label element;
            if (e.getTeam().equals(Team.OASPETE)) {

                element = new Label (e.toString());
            } else
                element = new Label("                            " + e.toString());

            container.getChildren().add(element);
        }
    }

    @Override
    public void update() {
        container.getChildren().clear();
        loadData();
    }
}
