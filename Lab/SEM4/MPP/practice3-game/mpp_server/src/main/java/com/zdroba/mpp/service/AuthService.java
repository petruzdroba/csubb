package com.zdroba.mpp.service;

import com.zdroba.mpp.entity.User;
import com.zdroba.mpp.exceptions.AlreadyExistsException;
import com.zdroba.mpp.exceptions.InvalidPasswordException;
import com.zdroba.mpp.exceptions.NotFoundException;
import com.zdroba.mpp.repository.IUserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {

    private final IUserRepository repository;

    public AuthService(IUserRepository repository) {
        this.repository = repository;
    }


    @Override
    public User login(String email, String password) throws NotFoundException, InvalidPasswordException, IllegalArgumentException {
        if(email.isEmpty() || password.isEmpty())
            throw new IllegalArgumentException("Invalid data");

        User user = repository.find(email);

        if (user == null)
            throw new NotFoundException(String.format("User with email: %s does not exist", email));

        if (!BCrypt.checkpw(password, user.getPassword()))
            throw new InvalidPasswordException("Passwords do not match");

        return user;
    }

    @Override
    public User register(String email, String name, String password) throws AlreadyExistsException, IllegalArgumentException {
        if(email.isEmpty() || password.isEmpty())
            throw new IllegalArgumentException("Invalid data");

        User existing = repository.find(email);

        if (existing != null)
            throw new AlreadyExistsException("User already exists");

        User user = new User(
                name,
                email,
                BCrypt.hashpw(password, BCrypt.gensalt())
        );

        Long id = repository.save(user);
        user.setId(id);

        return user;
    }
}
