package com.validators;

import com.domain.Message;
import com.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class MessageValidator implements Validator<Message>{
    protected List<String> errorMessages = new ArrayList<>();

    protected final StringValidator messageValidator = new StringValidator(1, 5000);
    protected final LocalDateTimeValidator dataValidator = new LocalDateTimeValidator(true, false);

    @Override
    public boolean validate(Message value) {
        errorMessages.clear();
        boolean valid = true;

        if(!messageValidator.validate(value.getMessage())){
            errorMessages.add("message: " + messageValidator.getErrorMessage());
            valid = false;
        }

        if(!dataValidator.validate(value.getData())){
            errorMessages.add("timestamp: " + dataValidator.getErrorMessage());
            valid = false;
        }

        return valid;
    }

    public void validateThrow(Message value) throws ValidationException {
        if(!validate(value)){
            throw new ValidationException(getErrorMessage());
        }
    }

    @Override
    public String getErrorMessage() {
        return String.join("\n", errorMessages);
    }
}
