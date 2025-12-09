package com.ui.controllers;

import com.domain.CurrentUser;
import com.domain.Message;
import com.domain.User;
import com.service.MessageService;
import com.service.UserService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SendMessageViewController {
    private UserService userService;
    private MessageService messageService;

    private Message replyToMessage;
    private boolean replyAll;

    @FXML private Label fromLabel;
    @FXML private TextField toField;
    @FXML private Label dateLabel;
    @FXML private Label replyLabel;
    @FXML private TextArea messageBody;

    public void setUserService(UserService service) {
        this.userService = service;
    }

    public void setMessageService(MessageService service) {
        this.messageService = service;
    }

    public void setReplyToMessage(Message message, boolean replyAll) {
        this.replyToMessage = message;
        this.replyAll=replyAll;

        messageBody.setText("RE: ");

        setRecipientsForReply();
    }

    @FXML
    private void initialize(){
        User currentUser = CurrentUser.getInstance().getUser();
        if(currentUser == null)
            return;
        fromLabel.setText(currentUser.getEmail());
    }

    private void setRecipientsForReply() {
        if (replyToMessage == null) return;

        List<User> recipients = replyAll
                ? replyToMessage.getTo()
                : Collections.singletonList(replyToMessage.getFrom());

        String emails = recipients.stream()
                .map(User::getEmail)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
        toField.setText(emails);
    }


    public void handleSend()  {
        try{
            if (replyToMessage == null) {
                String[] emailsArray = toField.getText().split(",");
                List<String> emails = Arrays.stream(emailsArray)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toList();
                messageService.sendMessage(emails, messageBody.getText());
            }
            else if(!replyAll){
                messageService.reply(replyToMessage.getId(), messageBody.getText());
            }else{
                messageService.replyAll(replyToMessage.getId(), messageBody.getText());
            }
            ((Stage) fromLabel.getScene().getWindow()).close();
        } catch (SQLException e) {
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
