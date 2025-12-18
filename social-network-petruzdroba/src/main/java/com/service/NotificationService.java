package com.service;

import com.domain.Notification;
import com.domain.User;
import com.exceptions.NotLoggedIn;
import com.exceptions.ValidationException;
import com.repo.AbstractDatabaseRepository;
import com.repo.NotificationRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NotificationService extends AbstractService<Long, Notification>{

    public NotificationService(AbstractDatabaseRepository<Long, Notification> repository) {
        super(repository);
    }

    public Collection<Notification> getAllNotifications(User user) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");

        return ((NotificationRepository)repository).getAllNotifications(user);
    }

    public Collection<Notification> getUnreadNotifications(User user) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");

        return ((NotificationRepository)repository).getUnreadNotifications(user);
    }

    public Collection<Long> getAllNotificationKeys(User user) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");

        return ((NotificationRepository)repository).getAllNotificationKeys(user);
    }

    public Collection<Long> getUnreadNotificationKeys(User user) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");

        return ((NotificationRepository)repository).getUnreadNotificationKeys(user);
    }

    public Collection<Notification> getAllNotificationsPage(User user, int offset, int limit) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");
        if (offset < 0 || limit < 1)
            throw new ValidationException("Offset or Limit values below 0");

        return ((NotificationRepository)repository).getAllNotificationsPage(user, offset, limit);
    }

    public Collection<Notification> getUnreadNotificationsPage(User user, int offset, int limit) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");
        if (offset < 0 || limit < 1)
            throw new ValidationException("Offset or Limit values below 0");

        return ((NotificationRepository)repository).getUnreadNotificationsPage(user, offset, limit);
    }

    public Collection<Long> getAllNotificationKeysPage(User user, int offset, int limit) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");
        if (offset < 0 || limit < 1)
            throw new ValidationException("Offset or Limit values below 0");

        return ((NotificationRepository)repository).getAllNotificationKeysPage(user, offset, limit);
    }

    public Collection<Long> getUnreadNotificationKeysPage(User user, int offset, int limit) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");
        if (offset < 0 || limit < 1)
            throw new ValidationException("Offset or Limit values below 0");

        return ((NotificationRepository)repository).getUnreadNotificationKeysPage(user, offset, limit);
    }

    public int pageCountAll(User user, int pageSize) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");
        if (pageSize < 1)
            throw new ValidationException("Page size must be >= 1.");

        return ((NotificationRepository)repository).pageCountAll(user, pageSize);
    }

    public int pageCountUnread(User user, int pageSize) {
        if (user == null)
            throw new NotLoggedIn("User must not be null.");
        if (pageSize < 1)
            throw new ValidationException("Page size must be >= 1.");

        return ((NotificationRepository)repository).pageCountUnread(user, pageSize);
    }

    private void pushObserver(List<User> users){
        List<Long> notifiedIds = new ArrayList<>(users.stream()
                .map(User::getId)
                .toList());

        observers.stream().filter(User.class::isInstance)
                .map(User.class::cast)
                .filter(o -> notifiedIds.contains(o.getId()))
                .forEach(User::update);
    }

    public void send(User to, String text) throws SQLException {
        if(to == null)
            throw new ValidationException("User does not exist");

        if(text.isEmpty())
            throw new ValidationException("Notification message cannot be empty");

        Notification notification = new Notification(to, text, LocalDateTime.now());
        repository.add(null, notification);

        pushObserver(List.of(to));
    }

    public void markRead(Notification notification) throws SQLException {
        if (notification == null)
            throw new ValidationException("Notification cannot be null");

        notification.setRead(true);
        repository.modify(notification.getId(),notification);

        pushObserver(List.of(notification.getTo()));
    }

    public void markRead(List<Notification> notifications) throws SQLException {
        if (notifications == null || notifications.isEmpty())
            throw new ValidationException("Notification cannot be null");

        for (Notification notif : notifications) {
            notif.setRead(true);
            repository.modify(notif.getId(),notif);
        }

        List<User> notifiedIds = notifications.stream().map(Notification::getTo).toList();
        pushObserver(notifiedIds);
    }

    public void markUnread(Notification notification) throws SQLException{
        if(notification == null)
            throw new ValidationException("Notification cannot be null");

        notification.setRead(false);
        repository.modify(notification.getId(),notification);

        pushObserver(List.of(notification.getTo()));
    }

}
