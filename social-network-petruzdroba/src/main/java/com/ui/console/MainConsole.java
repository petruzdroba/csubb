package com.ui.console;

import java.util.List;

public class MainConsole extends AbstractConsole {
    private final List<AbstractConsole> consoles;

    public MainConsole(List<AbstractConsole> consoles) {
        this.consoles = consoles;
    }

    @Override
    public void run() {
        boolean running = true;
        while (running) {
            showMenu();
            System.out.print("Select option: ");
            String choice = scanner.nextLine();

            try {
                int option = Integer.parseInt(choice);
                if (option == 0) {
                    running = false;
                } else if (option > 0 && option <= consoles.size()) {
                    consoles.get(option - 1).run();  // delegate to sub-console
                } else {
                    System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
        System.out.println("Exiting " + this);
    }

    @Override
    protected void showMenu() {
        System.out.println("\n==== Main Menu ====");
        System.out.println("0. Exit");
        for (int i = 0; i < consoles.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, consoles.get(i));
        }
    }

    @Override
    public String toString() {
        return "Main Console";
    }
}
