package com.service;

import com.domain.Card;
import com.domain.Duck;
import com.exceptions.RepositoryException;
import com.repo.AbstractDatabaseRepository;
import com.repo.UserRepository;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CardService extends AbstractService<Duck.TipRata, Card> {
    private final UserRepository userRepository;

    public CardService(AbstractDatabaseRepository<Duck.TipRata, Card> repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }

    /**
     *  Adauga un id de rata la un card anume
     *
     * @param duckId ID of the Duck
     * @param type   Duck.TipRata type (SWIMMING or FLYING)
     * @throws RepositoryException if the card does not exist
     */
    public void addDuckToCard(long duckId, Duck.TipRata type) throws RepositoryException, SQLException {
        Card card = repository.find(type);
        if (card == null) {
            throw new RepositoryException("Card of type " + type + " does not exist");
        }
        card.addDuck(duckId);
        repository.modify(type, card);
    }

    /**
     * Returns the average performance of all ducks in a Card.
     * Performance is calculated as the average of each duck's speed (viteza) and endurance (rezistenta),
     * then averaged across all ducks in the card.
     *
     * @param tip Duck.TipRata type (SWIMMING or FLYING)
     * @return the average performance, or 0.0 if no ducks exist in the card
     */
    public double getPerformantaMedie(Duck.TipRata tip) throws RepositoryException, SQLException {
        Card card = repository.find(tip);
        if (card == null || card.getMembri().isEmpty()) return 0.0;

        return card.getMembri().stream()
                .map(userId -> {
                    try {
                        return userRepository.find(userId);
                    } catch (SQLException e) {
                        throw new RepositoryException(e.getMessage());
                    }
                })
                .filter(Objects::nonNull)
                .filter(Duck.class::isInstance)
                .map(Duck.class::cast)
                .mapToDouble(duck -> (duck.getViteza() + duck.getRezistenta()) / 2.0)
                .average()
                .orElse(0.0);
    }

    /**
     * Returns the list of duck IDs in a Card.
     *
     * @param type Duck.TipRata type
     * @return list of duck IDs
     */
    public Collection<Duck> getDucksInCard(Duck.TipRata type) throws RepositoryException, SQLException {
        Card card = repository.find(type);
        if (card == null || card.getMembri().isEmpty()) return List.of();

        return card.getMembri().stream()
                .map(userId -> {
                    try {
                        return userRepository.find(userId);
                    } catch (SQLException e) {
                        throw new RepositoryException(e.getMessage());
                    }
                })
                .filter(Objects::nonNull)
                .filter(Duck.class::isInstance)
                .map(Duck.class::cast)
                .toList();
    }


    /**
     * Returns all cards in the repository.
     *
     * @return collection of Cards
     */
    public Collection<Card> getAllCards() {
        return List.copyOf(repository.getAll());
    }
}
