package com.ui.controllers;

import com.domain.Observer;
import com.domain.Request;
import com.domain.User;
import com.exceptions.NotLoggedIn;
import com.exceptions.ValidationException;
import com.service.FriendshipService;
import com.service.RequestService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RequestViewController implements Observer {

    private RequestService requestService;
    private FriendshipService friendshipService;
    private User loggedInUser;

    @FXML private Label userLabel;
    @FXML private ListView<Request> requestListView;
    @FXML private Label fromLabel;
    @FXML private Label dateLabel;
    @FXML private Label statusLabel;

    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Label pageLabel;

    @FXML private TabPane tabPane;
    @FXML private Tab receivedTab;
    @FXML private Tab sentTab;

    @FXML private Button acceptButton;
    @FXML private Button denyButton;
    @FXML private Button deleteButton;


    private boolean showingReceived = true;
    private boolean showingSent = false;
    private boolean addFriendsOpen = false;

    private final ObservableList<Request> requestItems = FXCollections.observableArrayList();
    private final List<Request> requests = new ArrayList<>();

    private int pageCount = 1;
    private final int pageSize = 10;

    public void setRequestService(RequestService service) {
        this.requestService = service;
    }

    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        if (loggedInUser != null) {
            userLabel.setText(loggedInUser.getUsername() + " (" + loggedInUser.getEmail() + ")");
            loggedInUser.addObserver(this);
            requestService.addObserver(loggedInUser);
        }
        loadCurrentPage();
        updatePageButtons();
    }

    @FXML
    public void initialize() {
        requestListView.setItems(requestItems);

        requestListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Request req, boolean empty) {
                super.updateItem(req, empty);

                if (empty || req == null) {
                    setText(null);
                    return;
                }

                boolean received = req.getTo().getId() == loggedInUser.getId();

                String name = received
                        ? req.getFrom().getUsername()
                        : req.getTo().getUsername();

                String prefix = received ? "From: " : "To: ";

                String date = req.getData()
                        .format(DateTimeFormatter.ofPattern("HH:mm dd MMM"));

                setText(prefix + name + "    " + date);
            }
        });


        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            showingReceived = newTab == receivedTab;
            showingSent = newTab == sentTab;
            pageCount = 1;
            hideAllActionButtons();
            loadCurrentPage();
        });

        requestListView.getSelectionModel().selectedItemProperty().addListener((obs, oldReq, newReq) -> {
            if (newReq == null) {
                clearDetails();
                return;
            }
            fromLabel.setText(newReq.getFrom().getEmail());
            dateLabel.setText(newReq.getData().format(DateTimeFormatter.ofPattern(" HH':'mm dd MMM yyyy")));
            statusLabel.setText(newReq.getStatus().toString());

            updateActionButtons(newReq);
        });
    }

    private void clearDetails() {
        fromLabel.setText("");
        dateLabel.setText("");
        statusLabel.setText("");
    }

    private void hideAllActionButtons() {
        acceptButton.setVisible(false);
        denyButton.setVisible(false);
        deleteButton.setVisible(false);
    }

    private void updateActionButtons(Request req) {
        hideAllActionButtons();

        if (showingReceived && req.getStatus() == Request.status.PENDING) {
            acceptButton.setVisible(true);
            denyButton.setVisible(true);
        }

        if (showingSent) {
            deleteButton.setVisible(true);
        }
    }

    public void loadData(List<Request> reqs) {
        requests.clear();
        requests.addAll(reqs);

        requestItems.clear();
        requestItems.addAll(reqs);
    }

    public void loadCurrentPage() {
        if (loggedInUser == null)
            throw new NotLoggedIn("User not logged in");

        int offset = (pageCount - 1) * pageSize;
        List<Request> pageData = new ArrayList<>();

        try {
            if (showingReceived) {
                pageData.addAll(requestService.getReceivedPage(loggedInUser, offset, pageSize));
            } else if (showingSent) {
                pageData.addAll(requestService.getSentPage(loggedInUser, offset, pageSize));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        pageLabel.setText(
                showingReceived ? "Received Page: " + pageCount :
                        "Sent Page: " + pageCount
        );

        loadData(pageData);
        updatePageButtons();
    }

    protected void updatePageButtons() {
        prevButton.setDisable(pageCount <= 1);

        int pageCountTotal = 1;
        if (showingReceived) pageCountTotal = requestService.pageCountReceived(loggedInUser, pageSize);
        if (showingSent) pageCountTotal = requestService.pageCountSent(loggedInUser, pageSize);

        nextButton.setDisable(pageCount >= pageCountTotal);
    }

    public void onPrevPage() {
        if (pageCount > 1) {
            pageCount--;
            loadCurrentPage();
        }
        updatePageButtons();
    }

    public void onNextPage() {
        int pageCountTotal = showingReceived ? requestService.pageCountReceived(loggedInUser, pageSize) :
                requestService.pageCountSent(loggedInUser, pageSize);

        if (pageCount < pageCountTotal) {
            pageCount++;
            loadCurrentPage();
        }
        updatePageButtons();
    }

    @FXML
    private void openFriendshipsWindow() {
        if (addFriendsOpen) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/friendship-paged-view-by-user.fxml"));
            Parent root = loader.load();
            FriendshipPagedViewByUserController controller = loader.getController();
            controller.setFriendshipService(friendshipService, loggedInUser);
            Scene scene = new Scene(root);

            scene.getStylesheets().add(getClass().getResource("/dark-theme.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Friends of " + loggedInUser.getUsername());
            stage.setScene(scene);

            addFriendsOpen = true;
            stage.setOnHidden(e -> addFriendsOpen = false);

            stage.show();
        } catch (IOException e) {
            showError("Cannot open friends view: " + e.getMessage());
        }
    }

    @FXML
    private void openAddFriends(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/send-friendship-view.fxml"));
            Parent root = loader.load();
            SendFriendshipView controller = loader.getController();
            controller.setRequestService(requestService);
            controller.setLoggedInUser(loggedInUser);
            Scene scene = new Scene(root);

            scene.getStylesheets().add(getClass().getResource("/dark-theme.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Add some friends!");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError("Cannot open friends view: " + e.getMessage());
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

    @FXML private void onAccept(){
        Request selected = requestListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try{
            requestService.accept(loggedInUser, selected);
        } catch (SQLException | ValidationException e) {
            showError(e.getMessage());
        }
    }

    @FXML private void onReject(){
        Request selected = requestListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try{
            requestService.deny(loggedInUser, selected);
        } catch (SQLException | ValidationException e) {
            showError(e.getMessage());
        }
    }

    @FXML private void onDelete(){
        Request selected = requestListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try{
            requestService.remove(loggedInUser, selected);
        } catch (SQLException | ValidationException e) {
            showError(e.getMessage());
        }
    }

    @Override
    public void update() {
        loadCurrentPage();
    }
}
