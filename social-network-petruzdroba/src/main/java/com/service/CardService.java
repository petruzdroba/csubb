package main.java.com.service;

import main.java.com.domain.Card;
import main.java.com.exceptions.ValidationException;
import main.java.com.repo.AbstractRepository;
import main.java.com.validators.CardValidator;

import java.util.Map;
import java.util.Set;

public class CardService extends AbstractService<Long, Card>{
    private final CardValidator cardValidator = new CardValidator();

    public CardService(AbstractRepository<Long, Card> repository) {
        super(repository);
    }

    public void add(long cardId, String cardNume){
        Card card = new Card(cardId, cardNume);
        cardValidator.validate(card);

        repository.add(cardId,card);
    }

    public void remove(long cardId) throws ValidationException {
        if(cardId < 0 )
            throw new ValidationException("Card id cannot be negative");
        repository.remove(cardId);
    }
}
