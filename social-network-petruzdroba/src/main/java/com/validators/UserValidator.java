package com.validators;

import com.domain.Persoana;
import com.domain.User;

import java.util.ArrayList;
import java.util.List;

public abstract class UserValidator<T extends User> implements Validator<T> {
    protected List<String> errorMessages = new ArrayList<>();

    protected final IntRangeValidator idValidator = new IntRangeValidator(0);
    protected final StringValidator usernameValidator = new StringValidator(3, 15);
    protected final StringValidator emailValidator = new StringValidator(5, 50);
    protected final StringValidator passwordValidator = new StringValidator(6, 20);


    @Override
    public boolean validate(T value) {
        errorMessages.clear();
        boolean valid = true;

        if (!idValidator.validate((int)value.getId())) {
            errorMessages.add("id: " + idValidator.getErrorMessage());
            valid = false;
        }

        if (!usernameValidator.validate(value.getUsername())) {
            errorMessages.add("Username: " + usernameValidator.getErrorMessage());
            valid = false;
        }

        if (!emailValidator.validate(value.getEmail())) {
            errorMessages.add("Email: " + emailValidator.getErrorMessage());
            valid = false;
        }

        if (!passwordValidator.validate(value.getPassword())) {
            errorMessages.add("Password: " + passwordValidator.getErrorMessage());
            valid = false;
        }

        return valid;
    }

    @Override
    public String getErrorMessage() {
        return String.join("\n", errorMessages);
    }
}
