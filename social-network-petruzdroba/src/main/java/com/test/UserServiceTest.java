package main.java.com.tests;

import main.java.com.domain.Card;
import main.java.com.domain.Duck;
import main.java.com.domain.Persoana;
import main.java.com.exceptions.RepositoryException;
import main.java.com.exceptions.ValidationException;
import main.java.com.repo.Repository;
import main.java.com.service.Service;

import java.time.LocalDate;

public class UserServiceTest {
    private final Repository repo = new Repository();
    private final Service service = new Service(repo);

    public void runAll() {
        testAddUser();
        testModifyUser();
        testDeleteUser();
        System.out.println("All UserService tests passed!");
    }

    private void testAddUser() {
        try {
            // Add Persoana with valid password
            service.addUser(1, "john123", "john@example.com", "password1",
                    "John", "Doe", LocalDate.of(1990,1,1), "Engineer", 5);

            // Add Duck with valid password
            service.addUser(2, "duckling", "duck@example.com", "quack12",
                    Duck.TipRata.FLYING, 50.0, 5.0, new Card(1, "Gold"));

            boolean found1 = repo.getAllUsers().stream().anyMatch(u -> u.getId() == 1L);
            boolean found2 = repo.getAllUsers().stream().anyMatch(u -> u.getId() == 2L);

            if (!found1) throw new AssertionError("Persoana not added!");
            if (!found2) throw new AssertionError("Duck not added!");

        } catch (ValidationException | RepositoryException e) {
            throw new RuntimeException("testAddUser failed: " + e.getMessage());
        }
    }

    private void testModifyUser() {
        try {
            // Modify Persoana
            service.modifyUser(1, "john456", "john_new@example.com", "pass1234",
                    "John", "Doe", LocalDate.of(1990,1,1), "Manager", 7);

            Persoana p = (Persoana) repo.getAllUsers().stream()
                    .filter(u -> u.getId() == 1).findFirst().orElseThrow();

            if (!p.getUsername().equals("john456")) throw new AssertionError("Persoana username not updated!");
            if (p.getNivelEmpatie() != 7) throw new AssertionError("Persoana empatie not updated!");

            // Modify Duck
            service.modifyUser(2, "duckpro", "duck_new@example.com", "quack123",
                    Duck.TipRata.SWIMMING, 60.0, 6.0, new Card(2,"Platinum"));

            Duck d = (Duck) repo.getAllUsers().stream()
                    .filter(u -> u.getId() == 2).findFirst().orElseThrow();

            if (!d.getUsername().equals("duckpro")) throw new AssertionError("Duck username not updated!");
            if (d.getViteza() != 60.0) throw new AssertionError("Duck viteza not updated!");

        } catch (ValidationException | RepositoryException e) {
            throw new RuntimeException("testModifyUser failed: " + e.getMessage());
        }
    }

    private void testDeleteUser() {
        try {
            service.deleteUser(1);
            service.deleteUser(2);

            boolean exists1 = repo.getAllUsers().stream().anyMatch(u -> u.getId() == 1);
            boolean exists2 = repo.getAllUsers().stream().anyMatch(u -> u.getId() == 2);

            if (exists1) throw new AssertionError("Persoana not deleted!");
            if (exists2) throw new AssertionError("Duck not deleted!");

        } catch (ValidationException | RepositoryException e) {
            throw new RuntimeException("testDeleteUser failed: " + e.getMessage());
        }
    }

    // main method to run tests
    public static void main(String[] args) {
        new UserServiceTest().runAll();
    }
}
