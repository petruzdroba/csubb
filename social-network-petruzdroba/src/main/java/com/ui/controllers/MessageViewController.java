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
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MessageViewController implements Observer {

    private UserService userService;
    private MessageService messageService;
    private User loggedInUser;

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

    @FXML private TabPane tabPane;
    @FXML private Tab receivedTab;
    @FXML private Tab sentTab;

    private boolean showingReceived = true;

    private final ObservableList<Message> messageItems = FXCollections.observableArrayList();
    private final List<Message> messages = new ArrayList<>();

    private int pageCount = 1;
    private final int pageSize = 10;

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

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            showingReceived = newTab == receivedTab;
            pageCount = 1;
            loadCurrentPage();
        });

        messageListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldMsg, newMsg) -> {
                    if (newMsg == null) {
                        clearDetails();
                        return;
                    }

                    fromLabel.setText(newMsg.getFrom().getEmail());
                    toLabel.setText(formatUsers(newMsg.getTo()));
                    dateLabel.setText(newMsg.getData().format(DateTimeFormatter.ofPattern(" HH':'mm dd MMM yyyy")));
                    if (newMsg.getReply() != null && newMsg.getReply().getMessage() != null) {
                        String replyText = newMsg.getReply().getMessage().replaceAll("\\s+", " ");
                        replyLabel.setText(replyText.length() > 50
                                ? replyText.substring(0, 50) + "..."
                                : replyText);
                    } else {
                        replyLabel.setText("-");
                    }
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
        if (messageService == null)
            return;

        if (loggedInUser == null)
            throw new NotLoggedIn("User not logged in");
        int offset = (pageCount - 1) * pageSize;

        List<Message> pageMessages;

        if (!showingReceived) {
            pageMessages = new ArrayList<>(messageService.getSentPage(loggedInUser, offset, pageSize));
        } else {
            pageMessages = new ArrayList<>(messageService.getReceivedPage(loggedInUser, offset, pageSize));
        }

        pageLabel.setText((showingReceived ? "Received" : "Sent") + " Page: " + pageCount);
        updatePageButtons();
        loadData(pageMessages);
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
        int pageCountTotal = !showingReceived
                ? messageService.pageCountSent(loggedInUser, pageSize)
                : messageService.pageCountReceived(loggedInUser, pageSize);
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
        int pageCountTotal = showingReceived
                ? messageService.pageCountReceived(loggedInUser, pageSize)
                : messageService.pageCountSent(loggedInUser, pageSize);

        if (pageCount < pageCountTotal) {
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

    @FXML
    public void showAllConveresation() throws SQLException {
        Message selectedMessage = messageListView.getSelectionModel().getSelectedItem();
        if (selectedMessage != null) {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/conversation-thread-view.fxml"));
                Parent root = loader.load();

                ConversationThreadViewController controller = loader.getController();
                controller.setLoggedInUser(loggedInUser);
                controller.setMessages(messageService.getThread(selectedMessage.getId()));

                Stage stage = new Stage();
                stage.setTitle("Conversation Thread");

                Scene scene = new Scene(root);
                String conversationTheme = getClass().getResource("/conversation.css").toExternalForm();
                scene.getStylesheets().add(conversationTheme);

                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                showError("Cannot load send message screen: " + e.getMessage());
            }

        } else {
            showError("No message selected!");
        }
    }


    @Override
    public void update() {
        System.out.println("Controller: im notified");
        loadCurrentPage();
    }
}
