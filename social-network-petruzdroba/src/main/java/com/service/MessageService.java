package com.service;

import com.domain.Message;
import com.domain.Observable;
import com.domain.Observer;
import com.domain.User;
import com.exceptions.NotLoggedIn;
import com.exceptions.ValidationException;
import com.repo.AbstractDatabaseRepository;
import com.repo.MessageRepository;
import com.repo.UserRepository;
import com.validators.MessageValidator;
import javafx.application.Platform;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageService extends AbstractService<Long, Message> {
    private final UserRepository userRepository;
    private final MessageValidator messageValidator = new MessageValidator();
    private final ExecutorService executor;

    public MessageService(AbstractDatabaseRepository<Long, Message> repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
        executor = Executors.newFixedThreadPool(4);
    }

    public Collection<Message> getReceived(User user) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");

        return ((MessageRepository) repository).getReceived(user);
    }

    public Collection<Message> getReceivedPage(User user, int offset, int limit) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");

        if (offset < 0 || limit < 1)
            throw new ValidationException("Offset or Limit values below 0");

        return ((MessageRepository) repository).getReceivedPage(user, offset, limit);
    }

    public Collection<Long> getReceivedKeys(User user) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");

        return ((MessageRepository) repository).getReceivedKeys(user);
    }

    public int pageCountReceived(User user, int pageSize) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");
        if (pageSize < 1)
            throw new ValidationException("Page size must be >= 1.");

        return ((MessageRepository) repository).pageCountReceived(user, pageSize);
    }

    public Collection<Message> getSent(User user) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");

        return ((MessageRepository) repository).getSent(user);
    }


    public Collection<Message> getSentPage(User user, int offset, int limit) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");

        if (offset < 0 || limit < 1)
            throw new ValidationException("Offset or Limit values below 0");

        return ((MessageRepository) repository).getSentPage(user, offset, limit);
    }

    public Collection<Long> getSentKeys(User user) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");

        return ((MessageRepository) repository).getSentKeys(user);
    }

    public int pageCountSent(User user, int pageSize) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");
        if (pageSize < 1)
            throw new ValidationException("Page size must be >= 1.");

        return ((MessageRepository) repository).pageCountSent(user, pageSize);
    }

    public List<Message> getThread(Long messageId) throws SQLException {
        List<Message> thread = ((MessageRepository) repository).getFullThread(messageId);
        thread.sort(Comparator.comparing(Message::getData));
        return thread;
    }

    private void pushObserver(User sender,List<User> receivers) {
        CompletableFuture.runAsync(()->{
            List<Long> notifiedIds = new ArrayList<>(receivers.stream()
                    .map(User::getId)
                    .toList());

            notifiedIds.add(sender.getId());

            observers.stream()
                    .filter(User.class::isInstance)
                    .map(User.class::cast)
                    .filter(o -> notifiedIds.contains(o.getId()))
                    .forEach(o -> Platform.runLater(o::update));
        },executor);
    }

    public CompletableFuture<Void> sendMessage(User from, List<String> receiverEmail, String text) throws CompletionException {
        return CompletableFuture.runAsync(()->{
            try{
                if (from == null)
                    throw new NotLoggedIn("No user logged in");


                List<User> receivers = new ArrayList<>();
                for (String email : receiverEmail) {
                    User u = userRepository.findByEmail(email);
                    if (u != null) receivers.add(u);
                }

                Message message = new Message(from, text, LocalDateTime.now(), receivers);
                messageValidator.validateThrow(message);

                repository.add(null, message);

                pushObserver(from, receivers);
            }catch(SQLException e){
                throw new CompletionException(e);
            }
        },executor);
    }

    public CompletableFuture<Void> replyAll(User from, Long messageId, String text) throws CompletionException {
        return CompletableFuture.runAsync(()->{
            try{
                if (from == null)
                    throw new NotLoggedIn("No user logged in");

                Message message = repository.find(messageId);//the message that is beign replyed to

                List<User> receivers = new ArrayList<>();

                receivers.add(message.getFrom());

                message.getTo().stream()
                        .filter(u -> u.getId() != from.getId())
                        .forEach(receivers::add);

                Message replyMessage = new Message(from, text, LocalDateTime.now(), message, receivers);
                messageValidator.validateThrow(replyMessage);

                repository.add(null, replyMessage);

                pushObserver(from, receivers);
            }catch(SQLException e){
                throw  new CompletionException(e);
            }
        },executor);
    }

    public CompletableFuture<Void> reply(User from, Long messageId, String text) throws CompletionException {
        return CompletableFuture.runAsync(()->{
            try{
                if (from == null)
                    throw new NotLoggedIn("No user logged in");

//                for(int i=0; i<=Long.MAX_VALUE; i++){
//
//                }

                Message message = repository.find(messageId);

                Message replyMessage = new Message(from, text, LocalDateTime.now(), message, List.of(message.getFrom()));
                messageValidator.validateThrow(replyMessage);

                repository.add(null, replyMessage);

                pushObserver(from, List.of(message.getFrom()));
            }catch (SQLException e){
                throw new CompletionException(e);
            }
        },executor);
    }
}
