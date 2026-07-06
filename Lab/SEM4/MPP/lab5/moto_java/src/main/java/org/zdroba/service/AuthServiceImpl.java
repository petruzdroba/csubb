package org.zdroba.service;

import org.mindrot.jbcrypt.BCrypt;
import org.zdroba.entity.User;
import org.zdroba.exceptions.AlreadyExistsException;
import org.zdroba.exceptions.InvalidPasswordException;
import org.zdroba.exceptions.NotFoundException;
import org.zdroba.repository.UserRepository;
import org.zdroba.repository.UserRepositoryImpl;

public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User logIn(String email, String password) throws NotFoundException, InvalidPasswordException {
        User user = userRepository.find(email);

        if(user == null)
            throw new NotFoundException(String.format("User with email: %s does not exist", email));

        if(!BCrypt.checkpw(password, user.getPassword()))
            throw new InvalidPasswordException("Passwords do not match");

        return user;
    }

    @Override
    public User register(String email, String password) throws AlreadyExistsException {
        User user = userRepository.find(email);

        if(user != null)
            throw new AlreadyExistsException(String.format("User with email: %s does exists", email));

        user = new User(email, BCrypt.hashpw(password, BCrypt.gensalt()));
        userRepository.add(user); // in repo, .add mutates the object by adding generated id

        return user;
    }
}
