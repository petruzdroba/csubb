package main.java.com.ui;

import main.java.com.exceptions.RepositoryException;
import main.java.com.service.CardService;

public class CardConsole extends AbstractConsole {
    private final CardService service;

    public CardConsole(CardService service) {
        this.service = service;
    }

    @Override
    public void run() {
        boolean running = true;
        while (running) {
            showMenu();
            System.out.print("Select option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "0" -> running = false;
                case "1" -> add();
                case "2" -> remove();
                case "3" -> length();
                default -> System.out.println("Invalid option. Try again.");
            }
        }
        System.out.println("Exiting Card Console...");
    }

    @Override
    protected void showMenu() {
        System.out.println("\n==== Card Menu ====");
        System.out.println("0. Exit");
        System.out.println("1. Add Card");
        System.out.println("2. Remove Card");
        System.out.println("3. Number of cards");
    }

    @Override
    public String toString() {
        return "Card Menu";
    }

    private void add() {
        try {
            System.out.print("Card ID: ");
            long cardId = Long.parseLong(scanner.nextLine());

            System.out.print("Card Name: ");
            String cardName = scanner.nextLine();

            service.add(cardId, cardName);
            System.out.println("Card added successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
        } catch (RepositoryException re) {
            System.out.println("Repository error: " + re.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private void remove() {
        try {
            System.out.print("Card ID: ");
            long cardId = Long.parseLong(scanner.nextLine());

            service.remove(cardId);
            System.out.println("Card removed successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
        } catch (RepositoryException re) {
            System.out.println("Repository error: " + re.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private void length(){
        System.out.println("Number of cards: " + service.getAll().toArray().length);
    }
}
