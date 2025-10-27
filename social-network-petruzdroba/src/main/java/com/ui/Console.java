package main.java.com.ui;

import main.java.com.domain.Card;
import main.java.com.domain.Duck;
import main.java.com.domain.Persoana;
import main.java.com.exceptions.RepositoryException;
import main.java.com.exceptions.ValidationException;
import main.java.com.service.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Console {
    private final Service service;
    private final Scanner scanner = new Scanner(System.in);

    public Console(Service service) {
        this.service = service;
    }

    public void start() {
        boolean running = true;
        while (running) {
            showMenu();
            System.out.print("Select option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addPersoana();
                case "2" -> addDuck();
                case "3" -> removeUser();
                case "4" -> addFriendship();
                case "5" -> removeFriendship();
                case "6" -> running = false;
                default -> System.out.println("Invalid option. Try again.");
            }
        }
        System.out.println("Exiting...");
    }

    private void showMenu() {
        System.out.println("\n==== Main Menu ====");
        System.out.println("1. Add Persoana");
        System.out.println("2. Add Duck");
        System.out.println("3. Remove User");
        System.out.println("4. Add Friendship");
        System.out.println("5. Remove Friendship");
        System.out.println("6. Exit");
    }

    private void addPersoana() {
        try {
            System.out.print("ID: ");
            long id = Long.parseLong(scanner.nextLine());

            System.out.print("Username: ");
            String username = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            System.out.print("Nume: ");
            String nume = scanner.nextLine();

            System.out.print("Prenume: ");
            String prenume = scanner.nextLine();

            System.out.print("Data nasterii (yyyy-mm-dd): ");
            LocalDate dataNasterii = LocalDate.parse(scanner.nextLine());

            System.out.print("Ocupatie: ");
            String ocupatie = scanner.nextLine();

            System.out.print("Nivel Empatie (0-10): ");
            int nivelEmpatie = Integer.parseInt(scanner.nextLine());

            service.addUser(id, username, email, password, nume, prenume, dataNasterii, ocupatie, nivelEmpatie);
            System.out.println("Persoana added successfully!");

        } catch (NumberFormatException | DateTimeParseException e) {
            System.out.println("Invalid input type. Please try again.");
        } catch (ValidationException ve) {
            System.out.println("Validation errors:\n" + ve.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private void addDuck() {
        try {
            System.out.print("ID: ");
            long id = Long.parseLong(scanner.nextLine());

            System.out.print("Username: ");
            String username = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            System.out.println("Tip Rata options: FLYING, SWIMMING, FLYING_AND_SWIMMING");
            System.out.print("Tip Rata: ");
            Duck.TipRata tip = Duck.TipRata.valueOf(scanner.nextLine().toUpperCase());

            System.out.print("Viteza (0.0 - 100.0): ");
            double viteza = Double.parseDouble(scanner.nextLine());

            System.out.print("Rezistenta (0.0 - 10.0): ");
            double rezistenta = Double.parseDouble(scanner.nextLine());

            System.out.print("Card ID: ");
            int cardId = Integer.parseInt(scanner.nextLine());

            System.out.print("Card Nume: ");
            String cardNume = scanner.nextLine();

            Card card = new Card(cardId, cardNume);
            service.addUser(id, username, email, password, tip, viteza, rezistenta, card);
            System.out.println("Duck added successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Please try again.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid enum value for Tip Rata.");
        } catch (ValidationException ve) {
            System.out.println("Validation errors:\n" + ve.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    // 🆕 New method for removing a user
    private void removeUser() {
        try {
            System.out.print("Enter User ID to remove: ");
            long userId = Long.parseLong(scanner.nextLine());

            service.deleteUser(userId);
            System.out.println("User removed successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Please enter a valid numeric ID.");
        } catch (ValidationException ve) {
            System.out.println("Validation error: " + ve.getMessage());
        } catch (RepositoryException re) {
            System.out.println("Repository error: " + re.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private void addFriendship() {
        try {
            System.out.print("User ID 1: ");
            long userId1 = Long.parseLong(scanner.nextLine());

            System.out.print("User ID 2: ");
            long userId2 = Long.parseLong(scanner.nextLine());

            service.addFriendship(userId1, userId2);
            System.out.println("Friendship added successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Please enter numeric IDs.");
        } catch (ValidationException ve) {
            System.out.println("Validation error: " + ve.getMessage());
        } catch (RepositoryException re) {
            System.out.println("Repository error: " + re.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }


    private void removeFriendship() {
        try {
            System.out.print("User ID 1: ");
            long userId1 = Long.parseLong(scanner.nextLine());

            System.out.print("User ID 2: ");
            long userId2 = Long.parseLong(scanner.nextLine());

            service.removeFriendship(userId1, userId2);
            System.out.println("Friendship removed successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Please enter numeric IDs.");
        } catch (ValidationException ve) {
            System.out.println("Validation error: " + ve.getMessage());
        } catch (RepositoryException re) {
            System.out.println("Repository error: " + re.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

}
