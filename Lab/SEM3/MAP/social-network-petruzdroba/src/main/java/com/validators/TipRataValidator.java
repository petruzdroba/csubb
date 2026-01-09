package com.validators;

import com.domain.Duck;

public class TipRataValidator implements Validator<Duck.TipRata> {
    private String errorMessage;

    @Override
    public boolean validate(Duck.TipRata value) {
        boolean valid = true;

        if (value == null) {
            errorMessage = "TipRata cannot be null";
            valid = false;
        }
        return valid;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
