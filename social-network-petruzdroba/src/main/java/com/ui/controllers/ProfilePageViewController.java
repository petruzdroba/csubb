package com.ui.controllers;

import com.domain.Observer;
import com.domain.ProfilePicture;
import com.domain.User;
import com.service.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Objects;

public class ProfilePageViewController implements Observer {

    private RequestService requestService;
    private UserService userService;
    private MessageService messageService;
    private FriendshipService friendshipService;
    private ProfilePictureService profilePictureService;

    private User displayUser;
    private User loggedInUser;

    private boolean friendshipWindow = false;

    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;

    @FXML private HBox actionButtonsBox;
    @FXML private Button sendMessageButton;
    @FXML private Button requestButton;

    @FXML private ImageView profileImageView;
    @FXML private Button changePictureButton;

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

    public void setProfilePictureService(ProfilePictureService profilePictureService) {
        this.profilePictureService = profilePictureService;
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
        loadProfileImage();
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
        changePictureButton.setVisible(ownProfile);
        changePictureButton.setManaged(ownProfile);

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
            controller.setProfilePictureService(profilePictureService);

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

    @FXML
    private void handleChangeProfilePicture() {
        if (loggedInUser == null || displayUser == null || profilePictureService == null) return;

        if (loggedInUser.getId() != displayUser.getId()) return;

        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new javafx.stage.FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        java.io.File file = fileChooser.showOpenDialog(profileImageView.getScene().getWindow());
        if (file != null && file.exists()) {
            try {
                byte[] imageBytes = java.nio.file.Files.readAllBytes(file.toPath());
                String contentType = java.nio.file.Files.probeContentType(file.toPath());

                profilePictureService.modify(displayUser.getId(), imageBytes, contentType);

                loadProfileImage();

            } catch (Exception e) {
                showError("Failed to update profile picture: " + e.getMessage());
            }
        }
    }

    private void loadProfileImage() {
        if (displayUser == null || profilePictureService == null) return;

        try {
            ProfilePicture pic = profilePictureService.find(displayUser.getId());
            javafx.scene.image.Image fxImage;

            if (pic != null && pic.getImage() != null && pic.getImage().length > 0) {
                fxImage = new javafx.scene.image.Image(new java.io.ByteArrayInputStream(pic.getImage()),
                        100, 100, true, true);
            } else {
                fxImage = new javafx.scene.image.Image(
                        Objects.requireNonNull(getClass().getResourceAsStream("/no_profile_picture.png")),
                        100, 100, true, true
                );
            }

            profileImageView.setImage(fxImage);

            double width = profileImageView.getBoundsInLocal().getWidth();
            double height = profileImageView.getBoundsInLocal().getHeight();
            double radius = Math.min(width, height) / 2;

            Circle clip = new Circle(width / 2, height / 2, radius);
            profileImageView.setClip(clip);

        } catch (Exception e) {
            profileImageView.setImage(new javafx.scene.image.Image(
                    Objects.requireNonNull(getClass().getResourceAsStream("/no_profile_picture.png")),
                    100, 100, true, true
            ));
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

    @Override
    public void update(String message) {
        updateRequestButton();
    }
}
