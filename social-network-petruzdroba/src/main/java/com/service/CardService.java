package main.java.com.service;

import main.java.com.domain.Card;
import main.java.com.domain.Duck;
import main.java.com.exceptions.RepositoryException;
import main.java.com.repo.AbstractRepository;
import main.java.com.repo.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CardService extends AbstractService<Duck.TipRata, Card> {
    private final UserRepository userRepository;

    public CardService(AbstractRepository<Duck.TipRata, Card> repository, UserRepository userRepository) {
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
    public void addDuckToCard(long duckId, Duck.TipRata type) throws RepositoryException {
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
    public double getPerformantaMedie(Duck.TipRata tip) {
        Card card = repository.find(tip);
        if (card == null || card.getMembri().isEmpty()) return 0.0;

        double total = 0.0;
        int count = 0;

        for (long duckId : card.getMembri()) {
            Duck duck = (Duck) userRepository.find(duckId);
            if (duck != null) {
                total += (duck.getViteza() + duck.getRezistenta()) / 2.0;
                count++;
            }
        }

        return count == 0 ? 0.0 : total / count;
    }


    /**
     * Returns the list of duck IDs in a Card.
     *
     * @param type Duck.TipRata type
     * @return list of duck IDs
     */
    public Collection<Duck> getDucksInCard(Duck.TipRata type) {
        Card card = repository.find(type);
        if (card == null) return List.of();

        List<Duck> ducks = new ArrayList<>();
        for (Long duckId : card.getMembri()) {
            Duck duck = (Duck) userRepository.find(duckId); // fetch actual Duck object
            if (duck != null) {
                ducks.add(duck);
            }
        }

        return ducks;
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
