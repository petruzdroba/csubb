package com.service;

import com.domain.*;
import com.exceptions.RepositoryException;
import com.exceptions.ValidationException;
import com.repo.AbstractDatabaseRepository;
import com.repo.AbstractRepository;
import com.repo.CardRepository;
import com.repo.FriendshipRepository;
import com.validators.DuckValidator;
import com.validators.PersoanaValidator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class UserService extends AbstractService<Long, User> {
    private final PersoanaValidator persoanaValidator = new PersoanaValidator();
    private final DuckValidator duckValidator = new DuckValidator();
    private final FriendshipRepository friendshipRepository;
    private final CardRepository cardRepository;

    public UserService(AbstractDatabaseRepository<Long, User> repository, FriendshipRepository friendshipRepository, CardRepository cardRepository) {
        super(repository);
        this.friendshipRepository = friendshipRepository;
        this.cardRepository = cardRepository;
    }

    /**
     * Adauga un obiect de tip {@link Persoana}(extensie a {@link User}) in users
     * <p>
     * Utilizeaza UserValidator ca sa valideze datele primite de la utilizator
     *
     * @param id           Identificatorul unic al persoanei.
     * @param username     Numele de utilizator asociat persoanei.
     * @param email        Adresa de email a persoanei.
     * @param password     Parola contului utilizator.
     * @param nume         Numele de familie al persoanei.
     * @param prenume      Prenumele persoanei.
     * @param dataNasterii Data nașterii persoanei.
     * @param ocupatie     Ocupația persoanei.
     * @throws ValidationException                          id negativ, string length negativ sau peste 50, nivel empatie negativ sau peste 10.
     * @throws com.exceptions.RepositoryException daca exista un alt utilizator {@link User} cu acelasi id
     * @see com.validators.PersoanaValidator
     * @see com.repo.AbstractRepository#add(Object, Object)
     */
    public void add(long id, String username, String email, String password,
                    String nume, String prenume, LocalDate dataNasterii,
                    String ocupatie, int nivelEmpatie) throws SQLException {
        Persoana user = new Persoana(id, username, email, password,
                nume, prenume, dataNasterii, ocupatie, nivelEmpatie);
        persoanaValidator.validateThrow(user);
        repository.add(id, user);
    }

    /**
     * Adauga un utilizator {@link Duck} (extensie {@link User})
     * <p>
     * Creeaza, valideaza un utilizator de tip Duck. Daca dependencyRepository exista {@link CardRepository},
     * se verifica daca cardId exista
     *
     * @param id         Identificatorul unic al Duck-ului.
     * @param username   Numele de utilizator al Duck-ului.
     * @param email      Email-ul Duck-ului.
     * @param password   Parola Duck-ului.
     * @param tip        Tipul de Duck (TipRata).
     * @param viteza     Valoarea vitezei Duck-ului.
     * @param rezistenta Valoarea rezistenței Duck-ului.
     * @throws ValidationException                          daca id negativ, string length negativ sau peste 50, TipRata nu apartine {@link com.domain.Duck.TipRata}.
     * @throws ValidationException                          daca nu exista cardul cu cardId, {@link Card}
     * @throws com.exceptions.RepositoryException daca exista un utilizator cu id
     * @see com.repo.AbstractRepository#add(Object, Object)
     * @see com.validators.DuckValidator
     */
    public void add(long id, String username, String email, String password,
                    Duck.TipRata tip, double viteza, double rezistenta) throws SQLException {
        Duck user;
        if (tip == Duck.TipRata.FLYING) {
            user = new FlyingDuck(id, username, email, password, tip, viteza, rezistenta);
        } else if (tip == Duck.TipRata.SWIMMING) {
            user = new SwimmingDuck(id, username, email, password, tip, viteza, rezistenta);
        } else {
            user = new SwimmingFlyingDuck(id, username, email, password, tip, viteza, rezistenta);
        }
        duckValidator.validateThrow(user);

        repository.add(id, user);

        try{
        cardRepository.addDuck(tip, id);
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    /**
     * Modifica un utilizator {@link Persoana} (extensie {@link User}) in repository.
     * <p>
     * Creeaza si valideaza un obiect Persoana cu datele primite. Foloseste {@link PersoanaValidator}
     * pentru validare inainte de actualizarea repository-ului.
     *
     * @param id           Identificatorul unic al persoanei.
     * @param username     Numele de utilizator asociat persoanei.
     * @param email        Adresa de email a persoanei.
     * @param password     Parola contului utilizator.
     * @param nume         Numele de familie al persoanei.
     * @param prenume      Prenumele persoanei.
     * @param dataNasterii Data nasterii persoanei.
     * @param ocupatie     Ocupatia persoanei.
     * @param nivelEmpatie Nivelul de empatie al persoanei (0-10).
     * @throws ValidationException                          Daca id-ul este negativ, lungimea string-urilor este negativa sau peste 50, sau nivelEmpatie este in afara intervalului 0-10.
     * @throws com.exceptions.RepositoryException Daca exista conflicte de id in repository sau persoana nu exista.
     * @see com.validators.PersoanaValidator
     * @see com.repo.AbstractRepository#modify(Object, Object)
     */
    public void modify(long id, String username, String email, String password, String nume, String prenume, LocalDate dataNasterii, String ocupatie, int nivelEmpatie) throws SQLException {
        Persoana user = new Persoana(id, username, email, password, nume, prenume, dataNasterii, ocupatie, nivelEmpatie);
        persoanaValidator.validateThrow(user);

        repository.modify(id, user);
    }

    /**
     * Modifica un utilizator {@link Duck} (extensie {@link User}) in repository.
     * <p>
     * Creeaza si valideaza un Duck cu datele primite. Daca exista {@link CardRepository}
     * ca dependencyRepository, se verifica daca cardId exista inainte de modificare.
     *
     * @param id         Identificatorul unic al Duck-ului.
     * @param username   Numele de utilizator al Duck-ului.
     * @param email      Email-ul Duck-ului.
     * @param password   Parola Duck-ului.
     * @param tip        Tipul de Duck (TipRata).
     * @param viteza     Valoarea vitezei Duck-ului.
     * @param rezistenta Valoarea rezistentei Duck-ului.
     * @throws ValidationException                          Daca id-ul este negativ, lungimea string-urilor este negativa sau peste 50, sau tipul nu este valid {@link Duck.TipRata}.
     * @throws ValidationException                          Daca cardId-ul nu exista in {@link CardRepository}.
     * @throws com.exceptions.RepositoryException Daca exista deja un utilizator cu acelasi id in repository.
     * @see com.repo.AbstractRepository#modify(Object, Object)
     * @see com.validators.DuckValidator
     */
    public void modify(long id, String username, String email, String password, Duck.TipRata tip, double viteza, double rezistenta) throws SQLException {
        Duck user;
        if (tip == Duck.TipRata.FLYING) {
            user = new FlyingDuck(id, username, email, password, tip, viteza, rezistenta);
        } else if (tip == Duck.TipRata.SWIMMING) {
            user = new SwimmingDuck(id, username, email, password, tip, viteza, rezistenta);
        } else {
            user = new SwimmingFlyingDuck(id, username, email, password, tip, viteza, rezistenta);
        }
        duckValidator.validateThrow(user);

        repository.modify(id, user);
    }

    /**
     * Sterge un utilizator din repository dupa id.
     * <p>
     * Metoda verifica daca id-ul este valid (pozitiv) si apoi sterge utilizatorul
     * din repository-ul corespunzator. Aceasta metoda functioneaza atat pentru
     * Duck cat si pentru Persoana/User.
     * Cascade delete cu prieteniile also -> ON CASCADE DELETE ON DATABASE
     *
     * @param userId Identificatorul unic al utilizatorului care trebuie sters.
     * @throws ValidationException                          Daca userId este negativ.
     * @throws com.exceptions.RepositoryException daca nu exista id-ul
     * @see com.repo.AbstractRepository#remove(Object)
     */
    public void remove(long userId) throws ValidationException, SQLException {
        if (userId < 0)
            throw new ValidationException("User id cannot be negative");
        repository.remove(userId);


        User user = repository.find(userId);
        if (user instanceof Duck duck) {
            try{
            cardRepository.removeDuck(duck.getTip(), userId);

            } catch (SQLException e) {
                throw new RepositoryException(e.getMessage());
            }
        }
    }

    /**
     * Cauta un utilizator dupa numele de utilizator.
     * <p>
     * Parcurge toate obiectele din repository si returneaza primul utilizator
     * al carui username se potriveste cu cel dat. Daca nu exista niciun utilizator
     * cu username-ul respectiv, returneaza null.
     *
     * @param username Numele de utilizator cautat.
     * @return Obiectul {@link User} corespunzator username-ului, sau null daca nu este gasit.
     * @see com.repo.AbstractRepository#getAll()
     */
    public User findUserByName(String username) {
        return repository.getAll().stream()
                .filter( u -> u.getUsername().equals(username))
                .findFirst().
                orElse(null);
    }

    public Collection<Duck> getAllDucks() {
        return repository.getAll().stream().filter(Duck.class::isInstance).map(Duck.class::cast).toList();
    }
}
