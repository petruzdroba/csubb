package main.java.com.service;

import main.java.com.domain.Card;
import main.java.com.domain.Duck;
import main.java.com.domain.Persoana;
import main.java.com.repo.Repository;
import main.java.com.validators.DuckValidator;
import main.java.com.validators.PersoanaValidator;

import java.time.LocalDate;

public class Service {
    private Repository repo;
    private final PersoanaValidator persoanaValidator = new PersoanaValidator();
    private final DuckValidator duckValidator = new DuckValidator();

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
}
