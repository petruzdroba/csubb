package com.ui.controllers;

import com.domain.Message;
import com.domain.Observer;
import com.domain.User;
import com.exceptions.NotLoggedIn;
import com.service.MessageService;
import com.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MessageViewController implements Observer {

    private UserService userService;
    private MessageService messageService;
    private User loggedInUser;

    @FXML private Button logOutButton;
    @FXML private Label userLabel;

    @FXML private ListView<Message> messageListView;

    @FXML private Label fromLabel;
    @FXML private Label toLabel;
    @FXML private Label dateLabel;
    @FXML private Label replyLabel;
    @FXML private TextArea messageBody;

    @FXML protected Button prevButton;
    @FXML protected Button nextButton;
    @FXML protected Label pageLabel;

    private final ObservableList<Message> messageItems = FXCollections.observableArrayList();
    private final List<Message> messages = new ArrayList<>();

    private int pageCount = 1;
    private int pageSize = 20;

    public void setUserService(UserService service) {
        this.userService = service;
    }

    public void setMessageService(MessageService service) {
        this.messageService = service;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        if (loggedInUser != null) {
            userLabel.setText(loggedInUser.getUsername() + " (" + loggedInUser.getEmail() + ")");
            loggedInUser.addObserver(this);
            messageService.addObserver(loggedInUser);
        }

        loadCurrentPage();
        updatePageButtons();
    }


    @FXML
    public void initialize() {
        messageListView.setItems(messageItems);

        messageListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldMsg, newMsg) -> {
                    if (newMsg == null) {
                        clearDetails();
                        return;
                    }

                    fromLabel.setText(newMsg.getFrom().getEmail());
                    toLabel.setText(formatUsers(newMsg.getTo()));
                    dateLabel.setText(newMsg.getData().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
                    replyLabel.setText(
                            newMsg.getReply() != null
                                    ? newMsg.getReply().getMessage().substring(0,20)+"..."
                                    : "-"
                    );
                    messageBody.setText(newMsg.getMessage());
                });
    }

    public void loadData(List<Message> msgs) {
        messages.clear();
        messages.addAll(msgs);

        messageItems.clear();
        messageItems.addAll(msgs);
    }



    private String formatUsers(List<User> users) {
        if (users == null || users.isEmpty()) return "-";

        StringBuilder sb = new StringBuilder();
        for (User u : users) {
            sb.append(u.getEmail()).append(", ");
        }
        sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    public void loadCurrentPage() {
        if (messageService == null) return;

        if (loggedInUser == null) throw new NotLoggedIn("User not logged in");
        int offset = (pageCount - 1) * pageSize;

        List<Message> pageMessages = new ArrayList<>(messageService.getReceivedPage(loggedInUser, offset, pageSize));
        loadData(pageMessages);
        pageLabel.setText("Page: " + pageCount);

    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/auth-view.fxml"));
            Parent root = loader.load();
            AuthViewController controller = loader.getController();
            controller.setUserService(userService);
            controller.setMessageService(messageService);

            Stage stage = (Stage) logOutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.sizeToScene();
            stage.show();
        } catch (IOException e) {
            showError("Cannot load login screen: " + e.getMessage());
        }
    }

    private void clearDetails() {
        fromLabel.setText("");
        toLabel.setText("");
        dateLabel.setText("");
        replyLabel.setText("");
        messageBody.setText("");
    }

    @FXML
    protected void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected void updatePageButtons() {
        prevButton.setDisable(pageCount <= 1);
        nextButton.setDisable(pageCount >= messageService.pageCountReceived(loggedInUser, pageSize));
    }

    public void onPrevPage() {
        if (pageCount > 1) {
            pageCount--;
            loadCurrentPage();
        }
        updatePageButtons();
    }

    public void onNextPage() {
        if (pageCount < messageService.pageCountReceived(loggedInUser, pageSize)) {
            pageCount++;
            loadCurrentPage();
        }
        updatePageButtons();
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

    private void openReplyWindow(Message original, boolean replyAll) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/send-message-view.fxml"));
            Parent root = loader.load();

            SendMessageViewController controller = loader.getController();
            controller.setUserService(userService);
            controller.setMessageService(messageService);
            controller.setReplyToMessage(original, replyAll);
            controller.setLoggedInUser(loggedInUser);

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
    public void handleReply() {
        int index = messageListView.getSelectionModel().getSelectedIndex();
        if (index < 0 || index >= messages.size()) {
            showError("Select a message to reply to");
            return;
        }
        openReplyWindow(messages.get(index), false);
    }

    @FXML
    public void handleReplyAll() {
        int index = messageListView.getSelectionModel().getSelectedIndex();
        if (index < 0 || index >= messages.size()) {
            showError("Select a message to reply to");
            return;
        }
        openReplyWindow(messages.get(index), true);
    }

    @Override
    public void update() {
        System.out.println("Controller: im notified");
        loadCurrentPage();
    }
}
