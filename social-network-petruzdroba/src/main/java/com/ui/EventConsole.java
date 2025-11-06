package main.java.com.ui;

import main.java.com.domain.Culoar;
import main.java.com.domain.RaceEvent;
import main.java.com.domain.User;
import main.java.com.exceptions.DomainException;
import main.java.com.service.EventService;
import main.java.com.repo.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class EventConsole extends AbstractConsole {
    private final EventService eventService;

    public EventConsole(EventService eventService) {
        this.eventService = eventService;
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
                case "1" -> subscribeUser();
                case "2" -> unsubscribeUser();
                case "3" -> startRace();
                default -> System.out.println("Invalid option. Try again.");
            }
        }
        System.out.println("Exiting Event Console...");
    }

    @Override
    public void showMenu() {
        System.out.println("\n==== Event Menu ====");
        System.out.println("0. Exit");
        System.out.println("1. Subscribe User to Race Event");
        System.out.println("2. Unsubscribe User from Race Event");
        System.out.println("3. Start Race");
    }

    @Override
    public String toString() {
        return "Event Menu";
    }

    private void subscribeUser() {
        try {
            System.out.print("Enter User ID to subscribe: ");
            long userId = Long.parseLong(scanner.nextLine());
            eventService.subscribe(userId);
            System.out.println("User subscribed successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input.");
        } catch (DomainException de) {
            System.out.println("Error: " + de.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private void unsubscribeUser() {
        try {
            System.out.print("Enter User ID to unsubscribe: ");
            long userId = Long.parseLong(scanner.nextLine());
            eventService.unsubscribe(userId);
            System.out.println("User unsubscribed successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input.");
        } catch (DomainException de) {
            System.out.println("Error: " + de.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private void startRace() {
        try {
            System.out.print("Enter number of lanes: ");
            int numLanes = Integer.parseInt(scanner.nextLine());

            Collection<Culoar> culoars = new ArrayList<>();
            for (int i = 1; i <= numLanes; i++) {
                System.out.print("Enter distance for lane " + i + ": ");
                int dist = Integer.parseInt(scanner.nextLine());
                culoars.add(new Culoar(dist, i));
            }

            culoars = culoars.stream()
                    .sorted(Comparator.comparingInt(Culoar::getDistanta))
                    .toList();

            eventService.startRace(culoars);
            System.out.println("Race started! Subscribers have been notified.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
        } catch (DomainException de) {
            System.out.println("Domain error: " + de.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }


}
