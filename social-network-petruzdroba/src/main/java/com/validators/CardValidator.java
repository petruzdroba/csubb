package main.java.com.validators;

import main.java.com.domain.Card;
import main.java.com.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class CardValidator implements Validator<Card>{
    protected List<String> errorMessages = new ArrayList<>();


    private final IntRangeValidator idValidator = new IntRangeValidator(0);
    private final StringValidator numeCardValidator = new StringValidator(0,50);

    @Override
    public boolean validate(Card value) {
        errorMessages.clear();
        boolean valid = true;

        if (!idValidator.validate((int)value.getId())) {
            errorMessages.add("Id Card: " + idValidator.getErrorMessage());
            valid = false;
        }

        if (!numeCardValidator.validate(value.getNumeCard())) {
            errorMessages.add("Nume Card: " + numeCardValidator.getErrorMessage());
            valid = false;
        }

        return valid;
    }

    public void validateThrow(Card value) throws ValidationException {
        if(!validate(value)){
         throw new ValidationException(getErrorMessage());
        }
    }

    @Override
    public String getErrorMessage() {
        return String.join("\n", errorMessages);
    }
}
