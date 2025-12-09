package com.service;

import com.domain.CurrentUser;
import com.domain.Message;
import com.domain.User;
import com.exceptions.NotLoggedIn;
import com.repo.AbstractDatabaseRepository;
import com.repo.UserRepository;
import com.validators.MessageValidator;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageService extends AbstractService<Long, Message> {
    private final UserRepository userRepository;
    private final MessageValidator messageValidator = new MessageValidator();

    public MessageService(AbstractDatabaseRepository<Long, Message> repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }

    public void sendMessage(List<String> receiverEmail, String text) throws SQLException {
        User from = CurrentUser.getInstance().getUser();
        if (from == null)
            throw new NotLoggedIn("No user logged in");


        List<User> receivers = new ArrayList<>();
        for (String email: receiverEmail) {
            User u = userRepository.findByEmail(email);
            if (u != null) receivers.add(u);
        }

        Message message = new Message(from, text, LocalDateTime.now(), receivers);
        messageValidator.validateThrow(message);

        repository.add(null, message);
    }


    public void replyAll(Long messageId, String text) throws SQLException {
        User from = CurrentUser.getInstance().getUser();
        if (from == null)
            throw new NotLoggedIn("No user logged in");

        Message message = repository.find(messageId);//the message that is beign replyed to

        List<User> replyRecipients = new ArrayList<>();

        replyRecipients.add(message.getFrom());

        message.getTo().stream()
                .filter(u -> u.getId() != from.getId())
                .forEach(replyRecipients::add);

        Message replyMessage = new Message(from, text, LocalDateTime.now(), message, replyRecipients);
        messageValidator.validateThrow(replyMessage);

        repository.add(null, replyMessage);
    }

    public void reply(Long messageId, String text) throws SQLException{
        User from = CurrentUser.getInstance().getUser();
        if (from == null)
            throw new NotLoggedIn("No user logged in");

        Message message = repository.find(messageId);

        Message replyMessage = new Message(from, text, LocalDateTime.now(), message, List.of(message.getFrom()));
        messageValidator.validateThrow(replyMessage);

        repository.add(null, replyMessage);
    }
}
