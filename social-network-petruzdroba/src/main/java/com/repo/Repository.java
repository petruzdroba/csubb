package main.java.com.repo;

import main.java.com.domain.User;
import main.java.com.exceptions.RepositoryException;

import java.util.HashMap;
import java.util.Map;

public class Repository {
    private String filePath;
    private Map<Long, User> users = new HashMap<>();

    public Repository(String filePath) {
        this.filePath = filePath;
    }

    public void addUser(User u) throws RepositoryException {
        if(users.containsKey(u.getId()))
            throw new RepositoryException("User already exists");
        users.put(u.getId(),u);
    }

    public void removeUser(long userId) throws RepositoryException{
        if(!users.containsKey(userId))
            throw new RepositoryException("User id dosent exist");
        users.remove(userId);
    }
}
