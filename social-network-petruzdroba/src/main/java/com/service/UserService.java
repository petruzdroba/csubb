package main.java.com.service;

import main.java.com.domain.Duck;
import main.java.com.domain.Persoana;
import main.java.com.domain.User;
import main.java.com.exceptions.ValidationException;
import main.java.com.repo.AbstractRepository;
import main.java.com.validators.DuckValidator;
import main.java.com.validators.PersoanaValidator;

import java.time.LocalDate;

public class UserService extends AbstractService<Long, User> {
    private final PersoanaValidator persoanaValidator =  new PersoanaValidator();
    private final DuckValidator duckValidator = new DuckValidator();

    public UserService(AbstractRepository<Long, User> repository, AbstractRepository<Object, Object> cardRepo) {
        super(repository, cardRepo);

    }

    public void add(long id, String username, String email, String password,
                    String nume, String prenume, LocalDate dataNasterii,
                    String ocupatie, int nivelEmpatie) {
        Persoana user = new Persoana(id, username, email, password,
                nume, prenume, dataNasterii, ocupatie, nivelEmpatie);
        persoanaValidator.validateThrow(user);
        repository.add(id, user);
    }

    public void add(long id, String username, String email, String password,
                    Duck.TipRata tip, double viteza, double rezistenta, long cardId) {
        Duck user = new Duck(id, username, email, password, tip, viteza, rezistenta, cardId);
        duckValidator.validateThrow(user);

        if(dependencyRepository != null && !dependencyRepository.getKeys().contains(cardId))
            throw new ValidationException("Card id not found");

        repository.add(id, user);
    }

    public void modify(long id, String username, String email, String password, String nume, String prenume, LocalDate dataNasterii, String ocupatie, int nivelEmpatie){
        Persoana user = new Persoana(id, username,email,password, nume,prenume,dataNasterii,ocupatie,nivelEmpatie);
        persoanaValidator.validateThrow(user);

        repository.modify(id, user);
    }

    public void modify(long id, String username, String email, String password, Duck.TipRata tip, double viteza, double rezistenta, long cardId){
        Duck user = new Duck(id, username,email,password, tip, viteza, rezistenta, cardId);
        duckValidator.validateThrow(user);

        if(dependencyRepository != null && !dependencyRepository.getKeys().contains(cardId))
            throw new ValidationException("Card id not found");

        repository.modify(id, user);
    }

    public void remove(long userId) throws ValidationException {
        if(userId < 0 )
            throw new ValidationException("User id cannot be negative");
        repository.remove(userId);
    }

    public User findUserByName(String username){
        for(User u: repository.getAll()){
            if(u.getUsername().equals(username))
                return u;
        }

        return null;
    }
}
