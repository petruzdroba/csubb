package main.java.com.ui;

import main.java.com.domain.Culoar;
import main.java.com.exceptions.DomainException;
import main.java.com.exceptions.ValidationException;
import main.java.com.service.EventService;

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
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "0" -> running = false;
                case "1" -> addEvent();
                case "2" -> removeEvent();
                case "3" -> subscribeUser();
                case "4" -> unsubscribeUser();
                case "5" -> startRace();
                default -> System.out.println("Invalid option.");
            }
        }
        System.out.println("Exiting Event Console...");
    }

    @Override
    public void showMenu() {
        System.out.println("\n==== Event Menu ====");
        System.out.println("0. Exit");
        System.out.println("1. Add Race Event");
        System.out.println("2. Remove Race Event");
        System.out.println("3. Subscribe User to Event");
        System.out.println("4. Unsubscribe User from Event");
        System.out.println("5. Start Race");
    }

    @Override
    public String toString() {
        return "Event Menu";
    }

    private void addEvent() {
        try {
            System.out.print("Enter Event ID: ");
            long eventId = Long.parseLong(scanner.nextLine());

            System.out.print("Enter number of lanes: ");
            int numLanes = Integer.parseInt(scanner.nextLine());

            Collection<Culoar> lanes = new ArrayList<>();
            for (int i = 1; i <= numLanes; i++) {
                System.out.print("Distance for lane " + i + ": ");
                int dist = Integer.parseInt(scanner.nextLine());
                lanes.add(new Culoar(dist, i));
            }

            lanes = lanes.stream().sorted(Comparator.comparingInt(Culoar::getDistanta)).toList();

            eventService.add(eventId, lanes);
            System.out.println("Event added successfully.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input.");
        } catch (ValidationException ve) {
            System.out.println("Validation error: " + ve.getMessage());
        }
    }

    private void removeEvent() {
        try {
            System.out.print("Enter Event ID: ");
            long eventId = Long.parseLong(scanner.nextLine());
            eventService.remove(eventId);
            System.out.println("Event removed.");
        } catch (ValidationException ve) {
            System.out.println("Validation error: " + ve.getMessage());
        }
    }

    private void subscribeUser() {
        try {
            System.out.print("Enter Event ID: ");
            long eventId = Long.parseLong(scanner.nextLine());
            System.out.print("Enter User ID: ");
            long userId = Long.parseLong(scanner.nextLine());
            eventService.subscribe(eventId, userId);
            System.out.println("User subscribed.");
        } catch (DomainException de) {
            System.out.println("Error: " + de.getMessage());
        }
    }

    private void unsubscribeUser() {
        try {
            System.out.print("Enter Event ID: ");
            long eventId = Long.parseLong(scanner.nextLine());
            System.out.print("Enter User ID: ");
            long userId = Long.parseLong(scanner.nextLine());
            eventService.unsubscribe(eventId, userId);
            System.out.println("User unsubscribed.");
        } catch (DomainException de) {
            System.out.println("Error: " + de.getMessage());
        }
    }

    private void startRace() {
        try {
            System.out.print("Enter Event ID: ");
            long eventId = Long.parseLong(scanner.nextLine());
            eventService.startRace(eventId);
            System.out.println("Race executed. Subscribers notified.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
