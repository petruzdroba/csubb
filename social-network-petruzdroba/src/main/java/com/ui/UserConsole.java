package main.java.com.ui;

import main.java.com.domain.Duck;
import main.java.com.domain.User;
import main.java.com.exceptions.RepositoryException;
import main.java.com.exceptions.ValidationException;
import main.java.com.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class UserConsole extends AbstractConsole{
    private final UserService service;

    public UserConsole(UserService service) {
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
                case "1" -> addPersoana();
                case "2" -> addDuck();
                case "3" -> remove();
                case "4" -> modifyPersoana();
                case "5" -> modifyDuck();
                case "6" -> findUserByUsername();
                case "7" -> showAll();
                default -> System.out.println("Invalid option. Try again.");
            }
        }
        System.out.println("Exiting...");
    }

    @Override
    public void showMenu() {
        System.out.println("\n==== Main Menu ====");
        System.out.println("0. Exit");
        System.out.println("1. Add Persoana");
        System.out.println("2. Add Duck");
        System.out.println("3. Remove User");
        System.out.println("4. Modify Persoana");
        System.out.println("5. Modify Duck");
        System.out.println("6. Find User by Username");
        System.out.println("7. Show all Users");
    }

    @Override
    public String toString() {
        return "User Menu";
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

            service.add(id, username, email, password, nume, prenume, dataNasterii, ocupatie, nivelEmpatie);
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

            service.add(id, username, email, password, tip, viteza, rezistenta);
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

    private void remove() {
        try {
            System.out.print("Enter User ID to remove: ");
            long userId = Long.parseLong(scanner.nextLine());

            service.remove(userId);
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

    private void modifyPersoana() {
        try {
            System.out.print("ID of Persoana to modify: ");
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

            service.modify(id, username, email, password, nume, prenume, dataNasterii, ocupatie, nivelEmpatie);
            System.out.println("Persoana modified successfully!");

        } catch (NumberFormatException | DateTimeParseException e) {
            System.out.println("Invalid input type. Please try again.");
        } catch (ValidationException ve) {
            System.out.println("Validation errors:\n" + ve.getMessage());
        } catch (RepositoryException re) {
            System.out.println("Repository error: " + re.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private void modifyDuck() {
        try {
            System.out.print("ID of Duck to modify: ");
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

            service.modify(id, username, email, password, tip, viteza, rezistenta);
            System.out.println("Duck modified successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Please try again.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid enum value for Tip Rata.");
        } catch (ValidationException ve) {
            System.out.println("Validation errors:\n" + ve.getMessage());
        } catch (RepositoryException re) {
            System.out.println("Repository error: " + re.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private void findUserByUsername() {
        System.out.print("Enter username to search: ");
        String username = scanner.nextLine();

        var user = service.findUserByName(username);
        if (user == null) {
            System.out.println("User not found!");
        } else {
            System.out.println(user.toString());
        }
    }

    private void showAll(){
        for(User u: service.getAll()){
            System.out.println(u);
        }
    }
}
