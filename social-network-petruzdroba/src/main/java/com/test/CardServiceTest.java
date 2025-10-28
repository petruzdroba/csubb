package main.java.com.test;

import main.java.com.domain.Card;
import main.java.com.exceptions.RepositoryException;
import main.java.com.repo.Repository;

public class CardServiceTest {
    private final Repository repo = new Repository();

    public void runAll() {
        testAddCard();
        testRemoveCard();
        System.out.println("All CardService tests passed!");
    }

    private void testAddCard() {
        try {
            Card c1 = new Card(1, "Gold");
            Card c2 = new Card(2, "Platinum");

            repo.addCard(c1);
            repo.addCard(c2);

            boolean found1 = repo.getAllCards().stream().anyMatch(c -> c.getId() == 1);
            boolean found2 = repo.getAllCards().stream().anyMatch(c -> c.getId() == 2);

            if (!found1) throw new AssertionError("Card Gold not added!");
            if (!found2) throw new AssertionError("Card Platinum not added!");

        } catch (RepositoryException e) {
            throw new RuntimeException("testAddCard failed: " + e.getMessage());
        }
    }

    private void testRemoveCard() {
        try {
            repo.removeCard(1);
            repo.removeCard(2);

            boolean exists1 = repo.getAllCards().stream().anyMatch(c -> c.getId() == 1);
            boolean exists2 = repo.getAllCards().stream().anyMatch(c -> c.getId() == 2);

            if (exists1) throw new AssertionError("Card Gold not deleted!");
            if (exists2) throw new AssertionError("Card Platinum not deleted!");

        } catch (RepositoryException e) {
            throw new RuntimeException("testRemoveCard failed: " + e.getMessage());
        }
    }

    // main method to run tests
    public static void main(String[] args) {
        new CardServiceTest().runAll();
    }
}
