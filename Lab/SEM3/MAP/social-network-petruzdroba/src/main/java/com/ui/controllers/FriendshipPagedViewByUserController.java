package com.ui.controllers;

import com.domain.Observable;
import com.domain.Observer;
import com.domain.User;
import com.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FriendshipPagedViewByUserController implements Observer {

    private FriendshipService friendshipService;

    private RequestService requestService;
    private UserService userService;
    private MessageService messageService;
    private ProfilePictureService profilePictureService;

    private User profileOwner;
    private User loggedInUser;
    private int pageCount = 1;
    private final int pageSize = 10;

    @FXML private TableView<User> tableView;
    @FXML private TableColumn<User, Long> idColumn;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, Void> actionColumn;
    @FXML private TableColumn<User, Void> viewProfileColumn;

    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Label pageLabel;

    public void setFriendshipService(FriendshipService service, User profileOwner, User loggedInUser) {
        this.friendshipService = service;
        this.profileOwner = profileOwner;
        this.loggedInUser = loggedInUser;

        profileOwner.addObserver(this);
        friendshipService.addObserver(profileOwner);

        loadCurrentPage();
    }

    public void setFriendshipService(FriendshipService service, User loggedInUser) {
        this.friendshipService = service;
        this.profileOwner = loggedInUser;
        this.loggedInUser = loggedInUser;

        profileOwner.addObserver(this);
        friendshipService.addObserver(profileOwner);

        loadCurrentPage();
    }

    public void setRequestService(RequestService requestService) {
        this.requestService = requestService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void setProfilePictureService(ProfilePictureService profilePictureService) {
        this.profilePictureService = profilePictureService;
    }

    @FXML
    public void initialize() {
        prevButton.setOnAction(e -> onPrevPage());
        nextButton.setOnAction(e -> onNextPage());
        setupTableColumns();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button removeButton = new Button("Remove");

            {
                removeButton.setOnAction(e -> {
                    User otherUser = getTableView().getItems().get(getIndex());
                    onRemoveFriendship(otherUser);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    removeButton.setDisable(!(profileOwner.getId() == loggedInUser.getId()));
                    setGraphic(removeButton);
                }
            }
        });

        viewProfileColumn.setCellFactory(col -> new TableCell<>() {
            private final Button profileButton = new Button("View Profile");

            {
                profileButton.setOnAction(e -> {
                    User selectedUser = getTableView().getItems().get(getIndex());
                    openProfile(selectedUser);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : profileButton);
            }
        });
    }

    public void loadData(List<Map.Entry<User, User>> friendships) {
        List<User> otherUsers = new ArrayList<>();
        for (Map.Entry<User, User> entry : friendships) {
            User user1 = entry.getKey();
            User user2 = entry.getValue();
            User other = (user1.getId() == profileOwner.getId()) ? user2 : user1;
            otherUsers.add(other);
        }

        ObservableList<User> data = FXCollections.observableList(otherUsers);
        tableView.setItems(data);
        pageLabel.setText("Page: " + pageCount);
        updatePageButtons();
    }

    public void loadCurrentPage() {
        if (friendshipService == null || profileOwner == null) return;

        int offset = (pageCount - 1) * pageSize;
        try {
            List<Map.Entry<User, User>> page = new ArrayList<>(friendshipService.getAllPrettyByUser(profileOwner, offset, pageSize));
            loadData(page);
        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

    private void updatePageButtons() {
        prevButton.setDisable(pageCount <= 1);
        int totalPages = friendshipService.pageCountByUser(profileOwner, pageSize);
        nextButton.setDisable(pageCount >= totalPages);
    }

    public void onPrevPage() {
        if (pageCount > 1) {
            pageCount--;
            loadCurrentPage();
        }
    }

    public void onNextPage() {
        int totalPages = friendshipService.pageCountByUser(profileOwner, pageSize);
        if (pageCount < totalPages) {
            pageCount++;
            loadCurrentPage();
        }
    }

    private void onRemoveFriendship(User otherUser) {
        try{
            friendshipService.remove(profileOwner.getId(), otherUser.getId());
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void openProfile(User userToView) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile-page-view.fxml"));
            Parent root = loader.load();

            ProfilePageViewController controller = loader.getController();
            controller.setUserService(userService);
            controller.setMessageService(messageService);
            controller.setRequestService(requestService);
            controller.setFriendshipService(friendshipService);
            controller.setLoggedInUser(loggedInUser);
            controller.setProfilePictureService(profilePictureService);
            controller.setDisplayUser(userToView);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/dark-theme.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle(userToView.getUsername() + "'s Profile");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError("Cannot open profile: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void update() {
        loadCurrentPage();
    }

    @Override
    public void update(String message) {
        loadCurrentPage();
    }

    public void removeObservers(){
        friendshipService.removeObserver(profileOwner);
        profileOwner.removeObserver(this);
    }
}
