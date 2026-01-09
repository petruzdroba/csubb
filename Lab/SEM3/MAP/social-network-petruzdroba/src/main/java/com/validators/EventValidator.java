package com.validators;

import com.domain.Event;
import com.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class EventValidator<T extends Event> implements Validator<T>{
    protected List<String> errorMessages = new ArrayList<>();

    protected final IntRangeValidator idValidator = new IntRangeValidator(0);


    @Override
    public boolean validate(T value) {
        errorMessages.clear();
        boolean valid = true;

        if (!idValidator.validate((int)value.getId())) {
            errorMessages.add("id: " + idValidator.getErrorMessage());
            valid = false;
        }

        return valid;
    }

    public void validateThrow(T value) throws ValidationException{
        if(!validate(value))
            throw new ValidationException(getErrorMessage());
    }

    @Override
    public String getErrorMessage() {
        return String.join("\n", errorMessages);
    }
}
