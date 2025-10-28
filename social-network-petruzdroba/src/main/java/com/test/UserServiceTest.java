package main.java.com.test;

import main.java.com.domain.Duck;
import main.java.com.domain.Persoana;
import main.java.com.domain.User;
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
        testFindUserByName();
        System.out.println("✅ All UserService tests passed!");
    }

    private void testFindUserByName() {
        try {
            // Add dummy users
            service.addUser(1, "alice", "alice@example.com", "password1",
                    "Alice", "Smith", LocalDate.of(1990, 1, 1), "Engineer", 5);
            service.addUser(2, "bob", "bob@example.com", "password2",
                    "Bob", "Brown", LocalDate.of(1992, 2, 2), "Doctor", 7);

            // Test findUserByName
            User u1 = service.findUserByName("alice");
            User u2 = service.findUserByName("bob");
            User u3 = service.findUserByName("charlie"); // does not exist

            if (u1 == null || !u1.getUsername().equals("alice")) throw new AssertionError("Failed to find Alice");
            if (u2 == null || !u2.getUsername().equals("bob")) throw new AssertionError("Failed to find Bob");
            if (u3 != null) throw new AssertionError("Found non-existent user Charlie");

        } catch (ValidationException | RepositoryException e) {
            throw new RuntimeException("testFindUserByName failed: " + e.getMessage());
        }
    }


    private void testAddUser() {
        try {
            // Add Persoana with valid password
            service.addUser(1, "john123", "john@example.com", "password1",
                    "John", "Doe", LocalDate.of(1990,1,1), "Engineer", 5);

            // Add Duck with valid password and cardId
            service.addUser(2, "duckling", "duck@example.com", "quack12",
                    Duck.TipRata.FLYING, 50.0, 5.0, 1L);

            boolean found1 = repo.getAllUsers().stream().anyMatch(u -> u.getId() == 1L);
            boolean found2 = repo.getAllUsers().stream().anyMatch(u -> u.getId() == 2L);

            if (!found1) throw new AssertionError("Persoana not added!");
            if (!found2) throw new AssertionError("Duck not added!");

            Duck d = (Duck) repo.getAllUsers().stream()
                    .filter(u -> u.getId() == 2).findFirst().orElseThrow();

            if (d.getCardId() != 1L)
                throw new AssertionError("Duck cardId not set correctly!");

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
                    Duck.TipRata.SWIMMING, 60.0, 6.0, 2L);

            Duck d = (Duck) repo.getAllUsers().stream()
                    .filter(u -> u.getId() == 2).findFirst().orElseThrow();

            if (!d.getUsername().equals("duckpro")) throw new AssertionError("Duck username not updated!");
            if (d.getViteza() != 60.0) throw new AssertionError("Duck viteza not updated!");
            if (d.getCardId() != 2L) throw new AssertionError("Duck cardId not updated!");

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
