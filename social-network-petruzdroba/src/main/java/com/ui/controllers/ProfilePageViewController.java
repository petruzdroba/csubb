package com.ui.controllers;

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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfilePageViewController {

    private RequestService requestService;
    private UserService userService;
    private MessageService messageService;
    private FriendshipService friendshipService;

    private User displayUser;
    private User loggedInUser;

    private boolean friendshipWindow = false;

    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;

    @FXML private VBox actionButtonsBox;
    @FXML private Button sendMessageButton;
    @FXML private Button sendFriendRequestButton;

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
        updateView();
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        updateView();
    }

    private void updateView() {
        if (displayUser == null) return;

        usernameLabel.setText(displayUser.getUsername());
        emailLabel.setText(displayUser.getEmail());

        boolean ownProfile =
                loggedInUser != null &&
                        displayUser.getId() == loggedInUser.getId();

        actionButtonsBox.setVisible(!ownProfile);
        actionButtonsBox.setManaged(!ownProfile);
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
            controller.setFriendshipService(friendshipService, displayUser);

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

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
