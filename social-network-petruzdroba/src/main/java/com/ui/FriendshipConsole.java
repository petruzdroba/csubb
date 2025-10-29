package main.java.com.ui;

import main.java.com.exceptions.RepositoryException;
import main.java.com.exceptions.ValidationException;
import main.java.com.service.FriendshipService;

public class FriendshipConsole extends AbstractConsole {
    private final FriendshipService service;

    public FriendshipConsole(FriendshipService service) {
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
                default -> System.out.println("Invalid option. Try again.");
            }
        }
        System.out.println("Exiting Friendship Console...");
    }

    @Override
    protected void showMenu() {
        System.out.println("\n==== Friendship Menu ====");
        System.out.println("0. Exit");
        System.out.println("1. Add Friendship");
        System.out.println("2. Remove Friendship");
    }

    private void add() {
        try {
            System.out.print("User ID 1: ");
            long userId1 = Long.parseLong(scanner.nextLine());

            System.out.print("User ID 2: ");
            long userId2 = Long.parseLong(scanner.nextLine());

            service.add(userId1, userId2);
            System.out.println("Friendship added successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
        } catch (ValidationException ve) {
            System.out.println("Validation error: " + ve.getMessage());
        } catch (RepositoryException re) {
            System.out.println("Repository error: " + re.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private void remove() {
        try {
            System.out.print("User ID 1: ");
            long userId1 = Long.parseLong(scanner.nextLine());

            System.out.print("User ID 2: ");
            long userId2 = Long.parseLong(scanner.nextLine());

            service.remove(userId1, userId2);
            System.out.println("Friendship removed successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
        } catch (ValidationException ve) {
            System.out.println("Validation error: " + ve.getMessage());
        } catch (RepositoryException re) {
            System.out.println("Repository error: " + re.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}
