package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.domain.*;
import org.example.service.EvenimentService;
import org.example.service.MeciService;

import java.io.IOException;
import java.util.List;

public class EntityController implements Observer {

    private MeciService meciService;
    private EvenimentService evenimentService;
    private User loggedInUser;

    public void setMeciService(MeciService meciService) {
        this.meciService = meciService;
        loadData();
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }


    public void setEvenimentService(EvenimentService evenimentService) {
        this.evenimentService = evenimentService;
        evenimentService.add(this);
    }

    @FXML private TableView<Meci> entityTable;
    @FXML private TableColumn<Meci, String> nGazdaColumn;
    @FXML private TableColumn<Meci, Integer> sGazdaColumn;
    @FXML private TableColumn<Meci, String> nOaspeteColumn;
    @FXML private TableColumn<Meci, Integer> sOaspeteColumn;

    @FXML private TableColumn<Meci, Void> actionColumn;

    public void setUp(){
        loadData();
    }

    private void loadData(){
        if(meciService == null) return;

        nGazdaColumn.setCellValueFactory(new PropertyValueFactory<>("numeGazda"));
        sGazdaColumn.setCellValueFactory(new PropertyValueFactory<>("scorGazda"));
        nOaspeteColumn.setCellValueFactory(new PropertyValueFactory<>("numeOaspete"));
        sOaspeteColumn.setCellValueFactory(new PropertyValueFactory<>("scorOaspete"));


        actionColumn.setCellFactory(col -> new TableCell<Meci, Void>() {
            private final Button btn = new Button("Action");

            {
                btn.setOnAction(e -> {
                    Meci entity = getTableView().getItems().get(getIndex());
                    try {
                        handleAction(entity);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
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

//        entityTable.getColumns().addAll(idColumn, actionColumn);
        ObservableList<Meci> data = FXCollections.observableList(meciService.get());
        entityTable.setItems(data);
    }

    private void handleAction(Meci meci) throws IOException {
        if(loggedInUser.getRole().equals(Role.ADMIN)){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin-add.fxml"));
            Scene scene = new Scene(loader.load());
            AdminAddController controller = loader.getController();
            controller.setEvenimentService(evenimentService);
            controller.setMeci(meci);

            Stage stage = new Stage();
            stage.setTitle("Add event");
            stage.setScene(scene);
            stage.show();
        }else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/spectator.fxml"));
            Scene scene = new Scene(loader.load());
            SpectatorController controller = loader.getController();
            controller.setEvenimentService(evenimentService);
            controller.setMeci(meci);

            Stage stage = new Stage();
            stage.setTitle("Spectator");
            stage.setScene(scene);
            stage.show();
        }
    }

    @Override
    public void update() {
        loadData();
    }
}
