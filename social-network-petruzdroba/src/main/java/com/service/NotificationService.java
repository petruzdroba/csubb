package com.service;

import com.domain.Notification;
import com.domain.User;
import com.exceptions.NotLoggedIn;
import com.exceptions.ValidationException;
import com.repo.AbstractDatabaseRepository;
import com.repo.NotificationRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
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

    public void send(User to, String text) throws SQLException {
        if(to == null)
            throw new ValidationException("User does not exist");

        if(text.isEmpty())
            throw new ValidationException("Notification message cannot be empty");

        Notification notification = new Notification(to, text, LocalDateTime.now());
        repository.add(null, notification);
        //push observer
    }

    public void markRead(Notification notification) throws SQLException {
        if (notification == null)
            throw new ValidationException("Notification cannot be null");

        notification.setRead(true);
        repository.modify(notification.getId(),notification);
    }

    public void markRead(List<Notification> notifications) throws SQLException {
        if (notifications == null || notifications.isEmpty())
            throw new ValidationException("Notification cannot be null");

        for (Notification notif : notifications) {
            notif.setRead(true);
            repository.modify(notif.getId(),notif);
        }
    }

    public void markUnread(Notification notification) throws SQLException{
        if(notification == null)
            throw new ValidationException("Notification cannot be null");

        notification.setRead(false);
        repository.modify(notification.getId(),notification);
    }

}
