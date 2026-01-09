package com.ui.console;

import com.domain.Card;
import com.domain.Duck;
import com.service.CardService;

import java.sql.SQLException;
import java.util.Collection;

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
                case "1" -> showAllCards();
                case "2" -> showDucksInCard();
                case "3" -> calculateAveragePerformance();
                default -> System.out.println("Invalid option. Try again.");
            }
        }
        System.out.println("Exiting Card Menu...");
    }

    @Override
    public void showMenu() {
        System.out.println("\n==== Card Menu ====");
        System.out.println("0. Exit");
        System.out.println("1. Show all Cards");
        System.out.println("2. Show Ducks in Card");
        System.out.println("3. Get Card Average Performance");
    }

    @Override
    public String toString() {
        return "Card Menu";
    }

    private void showAllCards() {
        Collection<Card> cards = service.getAllCards();
        if (cards.isEmpty()) {
            System.out.println("No cards available.");
        } else {
            cards.forEach(System.out::println);
        }
    }

    private void showDucksInCard() {
        try {
            Duck.TipRata tip = promptTipRata();
            if (tip == null) return;
            service.getDucksInCard(tip).forEach(System.out::println);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Tip Rata value.");
        } catch(Exception e){
            System.out.println("Unexpected error" + e.getMessage());
        }
    }

    private void calculateAveragePerformance() {
        try {
            Duck.TipRata tip = promptTipRata();
            if (tip == null) return;
            double avg = service.getPerformantaMedie(tip);
            System.out.printf("Average performance for %s: %.2f%n", tip, avg);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Tip Rata value.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private Duck.TipRata promptTipRata() {
        System.out.println("Tip Rata options: FLYING, SWIMMING");
        System.out.print("Tip Rata: ");
        String input = scanner.nextLine().toUpperCase();

        if (!input.equals("FLYING") && !input.equals("SWIMMING")) {
            System.out.println("Invalid Tip Rata value. Only FLYING or SWIMMING allowed.");
            return null;
        }

        return Duck.TipRata.valueOf(input);
    }
}
