package main.java.com.repo;

import main.java.com.domain.Card;
import main.java.com.domain.Duck;
import main.java.com.domain.Friendship;
import main.java.com.domain.User;
import main.java.com.exceptions.RepositoryException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Repository {
    protected Map<Long, User> users = new HashMap<>();
    protected Map<String, Friendship> friendships = new HashMap<>();
    protected Map<Long, Card> cards = new HashMap<>();


    public Repository() {}

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public Collection<Friendship> getAllFriendships(){
        return friendships.values();
    }

    public Collection<Card> getAllCards(){ return cards.values();}

    public void addUser(User u) throws RepositoryException {
        if(users.containsKey(u.getId()))
            throw new RepositoryException("User already exists");

        if( u instanceof Duck && !cards.containsKey(((Duck) u).getCardId()))
            throw new RepositoryException("Card with id nonExistent");

        users.put(u.getId(),u);
    }

    public void removeUser(long userId) throws RepositoryException{
        if(!users.containsKey(userId))
            throw new RepositoryException("User id dosent exist");
        users.remove(userId);
    }

    public void modifyUser(User u) throws  RepositoryException{
        if(!users.containsKey(u.getId()))
            throw new RepositoryException("User id dosent exist");
        users.put(u.getId(),u);
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

    public void addCard(Card c){
        if(cards.containsKey(c.getId()))
            throw new RepositoryException("Card already exists");
        cards.put(c.getId(),c);
    }

    public void removeCard(long cardId){
        if(!cards.containsKey(cardId))
            throw new RepositoryException("Card id dosent exist");
        cards.remove(cardId);
    }
}
