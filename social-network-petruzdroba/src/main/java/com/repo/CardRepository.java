package main.java.com.repo;

import main.java.com.domain.Card;
import main.java.com.exceptions.RepositoryException;

import java.io.*;
import java.util.Scanner;

public class CardRepository extends AbstractFileRepository<Long, Card>{
    public CardRepository(String filePath) {
        super(filePath);
    }

    @Override
    protected void loadFile() {
        try (Scanner sc = new Scanner(new File(filePath))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                long id = Long.parseLong(parts[0]);
                String name = parts[1];

                Card card = new Card(id, name);
                data.put(id, card);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Card file not found: " + filePath);
        } catch (RepositoryException e) {
            System.err.println("Card load error: " + e.getMessage());
        }
    }

    @Override
    protected void overwriteFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, false))) {
            for (Card c : getAll()) {
                pw.println(c.getId() + "," + c.getNumeCard());
            }
        } catch (IOException e) {
            System.err.println("Error writing cards: " + e.getMessage());
        }
    }
}
