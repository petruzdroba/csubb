package com.service;

import com.domain.ProfilePicture;
import com.exceptions.ValidationException;
import com.repo.AbstractDatabaseRepository;

import java.sql.SQLException;

public class ProfilePictureService extends AbstractService<Long, ProfilePicture>{


    public ProfilePictureService(AbstractDatabaseRepository<Long, ProfilePicture> repository) {
        super(repository);
    }

    public void add(Long userId, ProfilePicture picture) throws SQLException {
        if (userId == null || picture == null)
            throw new ValidationException("User id or picture is null");

        repository.add(userId, picture);
    }

    public void remove(Long userId) throws SQLException {
        if (userId == null)
            throw new ValidationException("User id is null");

        repository.remove(userId);
    }

    public void modify(Long userId, ProfilePicture picture) throws SQLException {
        if (userId == null || picture == null)
            throw new ValidationException("User id or picture is null");

        repository.modify(userId, picture);
    }

    public ProfilePicture find(Long userId) throws SQLException {
        if (userId == null)
            throw new ValidationException("User id is null");

        return repository.find(userId);
    }
}
