package main.java.com.repo;

import main.java.com.domain.Friendship;
import main.java.com.domain.User;
import main.java.com.exceptions.RepositoryException;

import java.util.HashMap;
import java.util.Map;

public class Repository {
    private String filePath;
    private Map<Long, User> users = new HashMap<>();
    private Map<String, Friendship> friendships = new HashMap<>();

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

    public void addFriendShip(Friendship friendship) throws RepositoryException{
        if(!users.containsKey(friendship.getUserId1()))
            throw new RepositoryException("User id 1 dosent exist");

        if(!users.containsKey(friendship.getUserId2()))
            throw new RepositoryException("User id 2 dosent exist");

        if(friendships.containsKey(friendship.getFriendshipId()))
            throw new RepositoryException("Frienship already exists");
        friendships.put(friendship.getFriendshipId(), friendship);
    }

    public void removeFriendship(String friendshipId) throws RepositoryException{
        if(!friendships.containsKey(friendshipId))
            throw new RepositoryException("FriendShip id dosent exist");
        friendships.remove(friendshipId);
    }
}
