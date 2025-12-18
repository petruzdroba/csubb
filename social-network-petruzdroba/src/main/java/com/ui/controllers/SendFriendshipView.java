package com.ui.controllers;

import com.domain.User;
import com.exceptions.NotLoggedIn;
import com.exceptions.ValidationException;
import com.service.RequestService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class SendFriendshipView {

    private RequestService requestService;
    private User loggedInUser;

    @FXML
    private TextField toField;

    public void setRequestService(RequestService requestService) {
        this.requestService = requestService;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    @FXML
    public void handleSend(){
        if(loggedInUser == null)
            throw new NotLoggedIn("User not logged IN");

        try{
            String email = toField.getText();
            requestService.send(loggedInUser, email);

            toField.clear();
        }catch (SQLException | ValidationException e){
            showError(e.getMessage());
        }
    }

    @FXML
    protected void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
