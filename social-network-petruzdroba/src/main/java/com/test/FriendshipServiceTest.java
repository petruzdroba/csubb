package main.java.com.test;

import main.java.com.domain.Friendship;
import main.java.com.domain.Persoana;
import main.java.com.exceptions.RepositoryException;
import main.java.com.exceptions.ValidationException;
import main.java.com.repo.Repository;
import main.java.com.service.Service;

import java.time.LocalDate;

public class FriendshipServiceTest {
    private final Repository repo = new Repository();
    private final Service service = new Service(repo);

    public void runAll() {
        testAddFriendship();
        testRemoveFriendship();
        System.out.println("All FriendshipService tests passed!");
    }

    private void testAddFriendship() {
        try {
            // Add dummy users
            repo.addUser(new Persoana(1,"u1","a@a.com","p","N","P", LocalDate.of(1990,1,1),"Job",5));
            repo.addUser(new Persoana(2,"u2","b@b.com","p","N","P", LocalDate.of(1992,2,2),"Job",6));

            service.addFriendship(1,2);

            boolean found = repo.getAllFriendships().stream()
                    .anyMatch(f -> (f.getUserId1() == 1 && f.getUserId2() == 2) || (f.getUserId1() == 2 && f.getUserId2() == 1));

            if (!found) throw new AssertionError("Friendship not added!");
        } catch (ValidationException | RepositoryException e) {
            throw new RuntimeException("testAddFriendship failed: " + e.getMessage());
        }
    }

    private void testRemoveFriendship() {
        try {
            Friendship f = repo.getAllFriendships().iterator().next();
            service.removeFriendship(f.getUserId1(), f.getUserId2());
            if (!repo.getAllFriendships().isEmpty()) throw new AssertionError("Friendship not removed!");
        } catch (ValidationException | RepositoryException e) {
            throw new RuntimeException("testRemoveFriendship failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        FriendshipServiceTest test = new FriendshipServiceTest();
        test.runAll();
    }
}
