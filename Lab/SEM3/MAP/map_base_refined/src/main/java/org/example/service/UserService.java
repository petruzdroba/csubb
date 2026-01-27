package org.example.service;

import org.example.domain.User;
import org.example.repo.UserRepo;

public class UserService {
    private final UserRepo userRepo;


    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User logIn(String username, String password){
        User user = userRepo.find(username);

        if(user.getPassword().equals(password))
            return user;
        return null;
    }
}
