package com.validators;

import com.domain.Friendship;
import com.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class FriendshipValidator implements Validator<Friendship> {
    private List<String> errorMessages = new ArrayList<>();
    private IntRangeValidator idValidator = new IntRangeValidator(0);


    @Override
    public boolean validate(Friendship value) {
        errorMessages.clear();
        boolean valid = true;

        if(value.getUserId1() == value.getUserId2()) {
            errorMessages.add("id: " + "cannot be the same");
            valid = false;
        }

        if (!idValidator.validate((int)value.getUserId1())) {
            errorMessages.add("id: " + idValidator.getErrorMessage());
            valid = false;
        }

        if (!idValidator.validate((int)value.getUserId2())) {
            errorMessages.add("id: " + idValidator.getErrorMessage());
            valid = false;
        }

        return valid;
    }

    public void validateThrow(Friendship value) throws ValidationException{
        if(!validate(value))
            throw new ValidationException(getErrorMessage());
    }

    @Override
    public String getErrorMessage() {
        return String.join("\n", errorMessages);
    }
}
