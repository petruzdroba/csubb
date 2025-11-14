package com.validators;

import com.domain.Card;
import com.domain.Duck;
import com.exceptions.ValidationException;

public class DuckValidator extends UserValidator<Duck> {
    private final TipRataValidator tipRataValidator = new TipRataValidator();
    private final DoubleValidator vitezaValidator = new DoubleValidator(0.0, 100.0);
    private final DoubleValidator rezistentaValidator = new DoubleValidator(0.0, 10.0);

    @Override
    public boolean validate(Duck value) {
        errorMessages.clear();
        boolean valid = true;

        if (!super.validate(value)) {
            valid = false;
            for (String err : super.getErrorMessage().split("\n")) {
                errorMessages.add(err);
            }
        }

        if (!tipRataValidator.validate(value.getTip())) {
            errorMessages.add("Tip Rata: " + tipRataValidator.getErrorMessage());
            valid = false;
        }

        if (!vitezaValidator.validate(value.getViteza())) {
            errorMessages.add("Viteza: " + vitezaValidator.getErrorMessage());
            valid = false;
        }

        if (!rezistentaValidator.validate(value.getRezistenta())) {
            errorMessages.add("Rezistenta: " + rezistentaValidator.getErrorMessage());
            valid = false;
        }

        return valid;
    }

    public void validateThrow(Duck value) throws ValidationException {
        if(!validate(value)){
            throw new ValidationException(getErrorMessage());
        }
    }

    @Override
    public String getErrorMessage() {
        return String.join("\n", errorMessages);
    }
}
