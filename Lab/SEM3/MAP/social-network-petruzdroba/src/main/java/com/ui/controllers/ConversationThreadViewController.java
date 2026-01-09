package com.ui.controllers;

import com.domain.Message;
import com.domain.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ConversationThreadViewController {

    @FXML
    private VBox messagesContainer;

    private User loggedInUser;

    private List<Message> messages = new ArrayList<>();

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages != null ? messages : new ArrayList<>();
        loadData();
    }

    private void loadData() {
        messagesContainer.getChildren().clear();

        for (Message msg : messages) {
            VBox messageVBox = new VBox();
            messageVBox.setSpacing(2);

            Label userLabel;
            Label messageLabel = new Label(msg.getMessage());
            messageLabel.setWrapText(true);
            messageLabel.setMaxWidth(400);

            HBox hbox = new HBox();
            hbox.setMaxWidth(Double.MAX_VALUE);

            boolean fromMe = msg.getFrom().getId() == loggedInUser.getId();
            boolean isPrivate = msg.getTo().size() == 1;

            String userText;
            if (msg.getReply() != null) {
                userText = msg.getFrom().getEmail() + " replying to " + msg.getReply().getFrom().getEmail();
            } else {
                userText = msg.getFrom().getEmail();
            }

            if (isPrivate) {
                userText += " (private)";
            }

            userText += " " + msg.getData().format(DateTimeFormatter.ofPattern("HH':'mm dd MMM"));
            userLabel = new Label(userText);

            if (fromMe) {
                userLabel.getStyleClass().addAll("message-user", "user-right");
                messageLabel.getStyleClass().add(isPrivate ? "right-highlight" : "message-right");
                hbox.getStyleClass().add("hbox-right");
            } else {
                userLabel.getStyleClass().addAll("message-user", "user-left");
                messageLabel.getStyleClass().add("message-left");
                hbox.getStyleClass().add("hbox-left");
            }

            messageVBox.getChildren().addAll(userLabel, messageLabel);
            hbox.getChildren().add(messageVBox);
            messagesContainer.getChildren().add(hbox);
        }
    }
}
