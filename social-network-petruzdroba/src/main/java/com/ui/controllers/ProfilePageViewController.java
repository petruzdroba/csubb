package com.ui.controllers;

import com.domain.Observer;
import com.domain.User;
import com.service.FriendshipService;
import com.service.MessageService;
import com.service.RequestService;
import com.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ProfilePageViewController implements Observer {

    private RequestService requestService;
    private UserService userService;
    private MessageService messageService;
    private FriendshipService friendshipService;

    private User displayUser;
    private User loggedInUser;

    private boolean friendshipWindow = false;

    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;

    @FXML private HBox actionButtonsBox;
    @FXML private Button sendMessageButton;
    @FXML private Button requestButton;

    public void setRequestService(RequestService requestService) {
        this.requestService = requestService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    public void setDisplayUser(User displayUser) {
        this.displayUser = displayUser;

        if (this.displayUser != null) {
            this.displayUser.addObserver(this);
            friendshipService.addObserver(this.displayUser);
            requestService.addObserver(this.displayUser);
        }

        updateView();
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;

        if (this.loggedInUser != null) {
            this.loggedInUser.addObserver(this);
            friendshipService.addObserver(this.loggedInUser);
            requestService.addObserver(this.loggedInUser);
        }

        updateView();
    }

    private void updateView() {
        if (displayUser == null) return;

        usernameLabel.setText(displayUser.getUsername());
        emailLabel.setText(displayUser.getEmail());

        updateRequestButton();
    }

    private void updateRequestButton() {
        if (loggedInUser == null || displayUser == null || friendshipService == null) {
            requestButton.setVisible(false);
            requestButton.setManaged(false);
            return;
        }

        boolean ownProfile = loggedInUser.getId() == displayUser.getId();

        sendMessageButton.setVisible(!ownProfile);
        sendMessageButton.setManaged(!ownProfile);
        requestButton.setVisible(!ownProfile);
        requestButton.setManaged(!ownProfile);

        if (!ownProfile) {
            try {
                boolean pendingRequest = requestService.exists(loggedInUser, displayUser);
                boolean friends = friendshipService.exists(loggedInUser.getId(), displayUser.getId());

                if (pendingRequest) {
                    requestButton.setText("Cancel Friend Request");
                } else if (friends) {
                    requestButton.setText("Remove Friendship");
                } else {
                    requestButton.setText("Send Friend Request");
                }
            } catch (SQLException e) {
                showError(e.getMessage());
            }
        }
    }

    @FXML
    private void openFriendshipsWindow() {
        if (friendshipWindow) return;

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/friendship-paged-view-by-user.fxml")
            );
            Parent root = loader.load();

            FriendshipPagedViewByUserController controller = loader.getController();
            controller.setFriendshipService(friendshipService, displayUser, loggedInUser);

            controller.setMessageService(messageService);
            controller.setUserService(userService);
            controller.setRequestService(requestService);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/dark-theme.css").toExternalForm()
            );

            Stage stage = new Stage();
            stage.setTitle("Friends of " + displayUser.getUsername());
            stage.setScene(scene);

            friendshipWindow = true;
            stage.setOnHidden(e -> {
                friendshipWindow = false;
                controller.removeObservers();
            });

            stage.show();
        } catch (IOException e) {
            showError("Cannot open friends view: " + e.getMessage());
        }
    }

    @FXML
    private void handleRequest() {
        try {
            if (requestService.exists(loggedInUser, displayUser)) {
                requestService.cancel(loggedInUser, displayUser);
            } else if (friendshipService.exists(loggedInUser.getId(), displayUser.getId())) {
                friendshipService.remove(loggedInUser.getId(), displayUser.getId());
            } else {
                requestService.send(loggedInUser, displayUser.getEmail());
            }

            updateRequestButton();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    public void handleSend() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/send-message-view.fxml"));
            Parent root = loader.load();

            SendMessageViewController controller = loader.getController();
            controller.setUserService(userService);
            controller.setMessageService(messageService);
            controller.setLoggedInUser(loggedInUser);
            controller.setRecipient(displayUser);

            Stage stage = new Stage();
            stage.setTitle("Send Message");

            Scene scene = new Scene(root);
            String darkTheme = getClass().getResource("/dark-theme.css").toExternalForm();
            scene.getStylesheets().add(darkTheme);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            showError("Cannot load send message screen: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void removeObservers() {
        if (displayUser != null) {
            displayUser.removeObserver(this);
            friendshipService.removeObserver(displayUser);
            requestService.removeObserver(displayUser);
        }

        if (loggedInUser != null) {
            loggedInUser.removeObserver(this);
            friendshipService.removeObserver(loggedInUser);
            requestService.removeObserver(loggedInUser);
        }
    }

    @Override
    public void update() {
        updateRequestButton();
    }
}
