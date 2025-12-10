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

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MessageService extends AbstractService<Long, Message> {
    private final UserRepository userRepository;
    private final MessageValidator messageValidator = new MessageValidator();

    public MessageService(AbstractDatabaseRepository<Long, Message> repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
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


    public void sendMessage(User from, List<String> receiverEmail, String text) throws SQLException {
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

        List<Long> receiverIds = receivers.stream()
                .map(User::getId)
                .toList();

        observers.stream()
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .filter(o -> receiverIds.contains(o.getId()))
                .forEach(User::update);
    }


    public void replyAll(User from, Long messageId, String text) throws SQLException {
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

        List<Long> receiverIds = receivers.stream().map(User::getId).toList();
        for (Observer o : observers) {
            if (o instanceof User u && receiverIds.contains(u.getId())) {
                u.update();
            }
        }
    }

    public void reply(User from, Long messageId, String text) throws SQLException {
        if (from == null)
            throw new NotLoggedIn("No user logged in");

        Message message = repository.find(messageId);

        Message replyMessage = new Message(from, text, LocalDateTime.now(), message, List.of(message.getFrom()));
        messageValidator.validateThrow(replyMessage);

        repository.add(null, replyMessage);

        List<Long> receiverIds = List.of(message.getFrom().getId());
        for (Observer o : observers) {
            if (o instanceof User u && receiverIds.contains(u.getId())) {
                u.update();
            }
        }
    }
}
