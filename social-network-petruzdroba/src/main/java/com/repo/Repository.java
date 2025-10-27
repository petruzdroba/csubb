package main.java.com.repo;

import main.java.com.domain.User;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    private String filePath;
    private List<User> users = new ArrayList<User>( );

    public Repository(String filePath) {
        this.filePath = filePath;
    }

    public void addUser(User u){
        users.add(u);
    }

    public void removeUser(User u){
        users.remove(u);
    }
}
