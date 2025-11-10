package com.repo;

import com.domain.Card;
import com.domain.Duck;
import com.exceptions.RepositoryException;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CardRepository extends AbstractFileRepository<Duck.TipRata, Card> {

    public CardRepository(String filePath) {
        super(filePath);
        if (!data.containsKey(Duck.TipRata.SWIMMING))
            data.put(Duck.TipRata.SWIMMING, new Card(1, "SwimMasters"));
        if (!data.containsKey(Duck.TipRata.FLYING))
            data.put(Duck.TipRata.FLYING, new Card(2, "SkyFlyers"));
    }

    public void addDuck(Duck.TipRata type, long duckId) throws RepositoryException {
        if (type == Duck.TipRata.SWIMMING || type == Duck.TipRata.FLYING_AND_SWIMMING) {
            Card swimmingCard = data.get(Duck.TipRata.SWIMMING);
            if (swimmingCard == null) throw new RepositoryException("Swimming card does not exist");
            swimmingCard.addDuck(duckId);
        }

        if (type == Duck.TipRata.FLYING || type == Duck.TipRata.FLYING_AND_SWIMMING) {
            Card flyingCard = data.get(Duck.TipRata.FLYING);
            if (flyingCard == null) throw new RepositoryException("Flying card does not exist");
            flyingCard.addDuck(duckId);
        }

        overwriteFile();
    }

    public void removeDuck(Duck.TipRata type, long duckId) throws RepositoryException {
        if (type == Duck.TipRata.SWIMMING || type == Duck.TipRata.FLYING_AND_SWIMMING) {
            Card swimmingCard = data.get(Duck.TipRata.SWIMMING);
            if (swimmingCard == null) throw new RepositoryException("Swimming card does not exist");
            if (!swimmingCard.getMembri().remove(duckId)) {
                throw new RepositoryException("Duck ID not found in Swimming card");
            }
        }

        if (type == Duck.TipRata.FLYING || type == Duck.TipRata.FLYING_AND_SWIMMING) {
            Card flyingCard = data.get(Duck.TipRata.FLYING);
            if (flyingCard == null) throw new RepositoryException("Flying card does not exist");
            if (!flyingCard.getMembri().remove(duckId)) {
                throw new RepositoryException("Duck ID not found in Flying card");
            }
        }

        overwriteFile();
    }



    @Override
    protected void loadFile() {
        File file = new File(filePath);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] parts = line.split(";");
                Duck.TipRata type = Duck.TipRata.valueOf(parts[0]);
                long cardId = Long.parseLong(parts[1]);
                String name = parts[2];
                Card card = new Card(cardId, name);

                if (parts.length > 3) {
                    List<Long> duckIds = Arrays.stream(parts[3].split(","))
                            .map(Long::parseLong)
                            .collect(Collectors.toList());
                    card.setDuckIds(duckIds);
                }

                data.put(type, card);
            }
        } catch (IOException e) {
            System.err.println("Error loading cards: " + e.getMessage());
        }
    }

    @Override
    protected void overwriteFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (var entry : data.entrySet()) {
                Duck.TipRata type = entry.getKey();
                Card card = entry.getValue();
                String ids = card.getMembri().stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));
                pw.println(type + ";" + card.getId() + ";" + card.getNumeCard() + ";" + ids);
            }
        } catch (IOException e) {
            System.err.println("Error writing cards: " + e.getMessage());
        }
    }
}
