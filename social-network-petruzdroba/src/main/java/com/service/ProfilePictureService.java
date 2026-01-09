package com.service;

import com.domain.ProfilePicture;
import com.exceptions.ValidationException;
import com.repo.AbstractDatabaseRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class ProfilePictureService extends AbstractService<Long, ProfilePicture> {

    public ProfilePictureService(AbstractDatabaseRepository<Long, ProfilePicture> repository) {
        super(repository);
    }

    public void add(Long userId, byte[] imageBytes, String contentType) throws SQLException {
        if (userId == null || imageBytes == null || contentType == null)
            throw new ValidationException("User id, image, or content type is null");

        ProfilePicture picture = new ProfilePicture(userId, imageBytes, contentType, LocalDateTime.now());
        repository.add(userId, picture);
    }

    public void modify(Long userId, byte[] imageBytes, String contentType) throws SQLException {
        if (userId == null || imageBytes == null || contentType == null)
            throw new ValidationException("User id, image, or content type is null");

        ProfilePicture existing = repository.find(userId);
        if (existing == null) {
            add(userId, imageBytes, contentType);
        } else {
            existing.setImage(imageBytes);
            existing.setContentType(contentType);
            existing.setUploadedAt(LocalDateTime.now());
            repository.modify(userId, existing);
        }
    }

    public void remove(Long userId) throws SQLException {
        if (userId == null)
            throw new ValidationException("User id is null");

        repository.remove(userId);
    }

    public ProfilePicture find(Long userId) throws SQLException {
        if (userId == null)
            throw new ValidationException("User id is null");

        return repository.find(userId);
    }
}
