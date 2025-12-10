package com.ui.controllers;

import com.domain.Message;
import com.domain.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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

            boolean isPrivate = msg.getReply()!= null && (msg.getTo().size() != msg.getReply().getTo().size()
                    || (msg.getTo().size() == 1 && !msg.getTo().contains(loggedInUser)));

            //email numme
            Label userLabel;
            if (msg.getReply() != null) {
                String replyText = msg.getFrom().getEmail() + " replying to " + msg.getReply().getFrom().getEmail();

                if (isPrivate) {
                    replyText += " (private)";
                }

                userLabel = new Label(replyText);
            } else {
                userLabel = new Label(msg.getFrom().getEmail());
            }


            userLabel.getStyleClass().add("message-user");
            if (msg.getFrom().getId() == loggedInUser.getId()) {
                userLabel.getStyleClass().add("user-right");
            } else {
                userLabel.getStyleClass().add("user-left");
            }

            Label messageLabel = new Label(msg.getMessage());
            messageLabel.setWrapText(true);
            messageLabel.setMaxWidth(400);

            if (msg.getFrom().getId() == loggedInUser.getId()) {
                if(isPrivate)
                    messageLabel.getStyleClass().add("right-highlight");
                else
                    messageLabel.getStyleClass().add("message-right");
            } else {
                messageLabel.getStyleClass().add("message-left");
            }

            messageVBox.getChildren().addAll(userLabel, messageLabel);

            HBox hbox = new HBox();
            hbox.setMaxWidth(Double.MAX_VALUE);
            if (msg.getFrom().getId() == loggedInUser.getId()) {
                hbox.getStyleClass().add("hbox-right");
            } else {
                hbox.getStyleClass().add("hbox-left");
            }

            hbox.getChildren().add(messageVBox);
            messagesContainer.getChildren().add(hbox);
        }
    }
}
