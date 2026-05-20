package org.zdroba.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.zdroba.entity.User;
import org.zdroba.exceptions.AlreadyExistsException;
import org.zdroba.exceptions.InvalidPasswordException;
import org.zdroba.exceptions.NotFoundException;
import org.zdroba.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public String login(String email, String password) throws InvalidPasswordException, NotFoundException {
        User user = userRepository.find(email);
        if (user == null)
            throw new NotFoundException(String.format("User with email: %s does not exist", email));
        if (!BCrypt.checkpw(password, user.getPassword()))
            throw new InvalidPasswordException("Passwords do not match");
        return jwtService.generateToken(user.getId());
    }

    public String register(String email, String password) throws AlreadyExistsException {
        User user = userRepository.find(email);
        if (user != null)
            throw new AlreadyExistsException(String.format("User with email: %s already exists", email));
        user = new User(email, BCrypt.hashpw(password, BCrypt.gensalt()));
        userRepository.add(user);
        return jwtService.generateToken(user.getId());
    }
}
