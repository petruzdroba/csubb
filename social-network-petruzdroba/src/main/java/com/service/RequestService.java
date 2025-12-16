package com.service;

import com.domain.Request;
import com.domain.User;
import com.exceptions.NotLoggedIn;
import com.exceptions.ValidationException;
import com.repo.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collection;

public class RequestService extends AbstractService<Long, Request>{
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;


    public RequestService(AbstractDatabaseRepository<Long, Request> repository, UserRepository userRepository, FriendshipRepository friendshipRepository) {
        super(repository);
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    public Collection<Request> getReceived(User user) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");

        return ((RequestRepository) repository).getReceived(user);
    }


    public Collection<Request> getReceivedPage(User user, int offset, int limit) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");

        if (offset < 0 || limit < 1)
            throw new ValidationException("Offset or Limit values below 0");

        return ((RequestRepository) repository).getReceivedPage(user, offset, limit);
    }

    public Collection<Long> getReceivedKeys(User user) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");

        return ((RequestRepository) repository).getReceivedKeys(user);
    }

    public int pageCountReceived(User user, int pageSize) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");
        if (pageSize < 1)
            throw new ValidationException("Page size must be >= 1.");

        return ((RequestRepository) repository).pageCountReceived(user, pageSize);
    }

    public Collection<Request> getSent(User user) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");

        return ((RequestRepository) repository).getSent(user);
    }


    public Collection<Request> getSentPage(User user, int offset, int limit) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");

        if (offset < 0 || limit < 1)
            throw new ValidationException("Offset or Limit values below 0");

        return ((RequestRepository) repository).getSentPage(user, offset, limit);
    }

    public Collection<Long> getSentKeys(User user) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");

        return ((RequestRepository) repository).getSentKeys(user);
    }

    public int pageCountSent(User user, int pageSize) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");
        if (pageSize < 1)
            throw new ValidationException("Page size must be >= 1.");

        return ((RequestRepository) repository).pageCountSent(user, pageSize);
    }

    public void send(User from, String email) throws SQLException {
        if (from == null)
            throw new NotLoggedIn("No user logged in");
        User to = userRepository.findByEmail(email);

        Request request = new Request(from, to, Request.status.PENDING, LocalDateTime.now());
        repository.add(null, request);

        //pushObserver here
    }

    public void remove(User currentUser, Request request) throws SQLException {
        if (currentUser == null)
            throw new NotLoggedIn("No user logged in");

        if(currentUser.getId() != request.getFrom().getId())
            throw new ValidationException("Just reject the request, dont delete from the other end");

        repository.remove(request.getId());
    }

    public void accept(User currentUser, Request request) throws SQLException {
        if (currentUser == null)
            throw new NotLoggedIn("No user logged in");

        if(currentUser.getId() != request.getTo().getId())
            throw new ValidationException("You cannot accept others requests for them");

        request.setStatus(Request.status.ACCEPTED);
        repository.modify(request.getId(), request);
    }

    public void deny(User currentUser, Request request) throws SQLException {
        if (currentUser == null)
            throw new NotLoggedIn("No user logged in");

        if(currentUser.getId() != request.getTo().getId())
            throw new ValidationException("You cannot deny others requests for them");

        request.setStatus(Request.status.REJECTED);
        repository.modify(request.getId(), request);
    }
}
