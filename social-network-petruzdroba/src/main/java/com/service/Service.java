package main.java.com.service;

import main.java.com.domain.Card;
import main.java.com.domain.Duck;
import main.java.com.domain.Friendship;
import main.java.com.domain.Persoana;
import main.java.com.exceptions.ValidationException;
import main.java.com.repo.Repository;
import main.java.com.validators.DuckValidator;
import main.java.com.validators.FriendshipValidator;
import main.java.com.validators.PersoanaValidator;

import java.time.LocalDate;

public class Service {
    private final Repository repo;
    private final PersoanaValidator persoanaValidator = new PersoanaValidator();
    private final DuckValidator duckValidator = new DuckValidator();
    private final FriendshipValidator friendshipValidator = new FriendshipValidator();

    public Service(Repository repo) {
        this.repo = repo;
    }

    public void addUser(long id, String username, String email, String password, String nume, String prenume, LocalDate dataNasterii, String ocupatie, int nivelEmpatie){
        Persoana user = new Persoana(id, username,email,password, nume,prenume,dataNasterii,ocupatie,nivelEmpatie);
        persoanaValidator.validateThrow(user);

        repo.addUser(user);
    }

    public void addUser(long id, String username, String email, String password, Duck.TipRata tip, double viteza, double rezistenta, Card card){
        Duck user = new Duck(id, username,email,password, tip, viteza, rezistenta, card);
        duckValidator.validateThrow(user);

        repo.addUser(user);
    }

    public void deleteUser(long userId) throws ValidationException{
        if(userId < 0 )
            throw new ValidationException("User id cannot be negative");
        repo.removeUser(userId);
    }

    public void modifyUser(long id, String username, String email, String password, String nume, String prenume, LocalDate dataNasterii, String ocupatie, int nivelEmpatie){
        Persoana user = new Persoana(id, username,email,password, nume,prenume,dataNasterii,ocupatie,nivelEmpatie);
        persoanaValidator.validateThrow(user);

        repo.modifyUser(user);
    }

    public void modifyUser(long id, String username, String email, String password, Duck.TipRata tip, double viteza, double rezistenta, Card card){
        Duck user = new Duck(id, username,email,password, tip, viteza, rezistenta, card);
        duckValidator.validateThrow(user);

        repo.modifyUser(user);
    }

    public void addFriendship(long userId1, long userId2){
        Friendship friendship = new Friendship(userId1, userId2);
        friendshipValidator.validate(friendship);

        repo.addFriendShip(friendship);
    }

    public void removeFriendship(long userId1, long userId2){
        Friendship friendship = new Friendship(userId1, userId2);
        friendshipValidator.validate(friendship);

        repo.removeFriendship(friendship.getFriendshipId());
    }
}
