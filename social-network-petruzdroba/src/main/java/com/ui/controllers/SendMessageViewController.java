package com.ui.controllers;

import com.domain.Message;
import com.domain.User;
import com.service.MessageService;
import com.service.UserService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SendMessageViewController {
    private UserService userService;
    private MessageService messageService;
    private User loggedInUser;

    private Message replyToMessage;
    private boolean replyAll;

    @FXML
    private Label fromLabel;
    @FXML
    private TextField toField;
    @FXML
    private Label dateLabel;
    @FXML
    private Label replyLabel;
    @FXML
    private TextArea messageBody;

    public void setUserService(UserService service) {
        this.userService = service;
    }

    public void setMessageService(MessageService service) {
        this.messageService = service;
    }

    public void setReplyToMessage(Message message, boolean replyAll) {
        this.replyToMessage = message;
        this.replyAll = replyAll;

        messageBody.setText("RE: ");
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        if (fromLabel != null) {
            fromLabel.setText(user.getEmail());
            setRecipientsForReply();
        }
    }

    @FXML
    private void initialize() {

    }

    public void setRecipient(User user) {
        if (user == null) return;
        toField.setText(user.getEmail());
    }


    private void setRecipientsForReply() {
        if (replyToMessage == null) return;


        List<User> recipients;

        if (replyAll) {
            recipients = new ArrayList<>();
            recipients.add(replyToMessage.getFrom());

            for (User u : replyToMessage.getTo()) {
                if (u.getId() != loggedInUser.getId() && !recipients.contains(u)) {
                    recipients.add(u);
                }
            }
        } else {
            recipients = Collections.singletonList(replyToMessage.getFrom());
        }

        String emails = recipients.stream()
                .map(User::getEmail)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
        toField.setText(emails);
    }


    public void handleSend() {
        if (replyToMessage == null) {
            String[] emailsArray = toField.getText().split(",");
            List<String> emails = Arrays.stream(emailsArray)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();
            messageService.sendMessage(loggedInUser, emails, messageBody.getText())
                    .thenRun(() -> Platform.runLater(() -> {
                        ((Stage) fromLabel.getScene().getWindow()).close();
                    }))
                    .exceptionally(ex -> {
                        Platform.runLater(() -> showError(ex.getCause().getMessage()));
                        return null;
                    });

        } else if (!replyAll) {
            messageService.reply(loggedInUser, replyToMessage.getId(), messageBody.getText())
                    .thenRun(() -> Platform.runLater(() -> {
                        ((Stage) fromLabel.getScene().getWindow()).close();
                    }))
                    .exceptionally(ex -> {
                        Platform.runLater(() -> showError(ex.getCause().getMessage()));
                        return null;
                    });
        } else {
            messageService.replyAll(loggedInUser, replyToMessage.getId(), messageBody.getText())
                    .thenRun(() -> Platform.runLater(() -> {
                        ((Stage) fromLabel.getScene().getWindow()).close();
                    }))
                    .exceptionally(ex -> {
                        Platform.runLater(() -> showError(ex.getCause().getMessage()));
                        return null;
                    });
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
