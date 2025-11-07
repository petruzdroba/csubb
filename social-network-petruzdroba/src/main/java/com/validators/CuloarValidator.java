package main.java.com.validators;

import main.java.com.domain.Culoar;
import main.java.com.domain.Persoana;
import main.java.com.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class CuloarValidator implements Validator<Culoar>{
    protected List<String> errorMessages = new ArrayList<>();

    private final IntRangeValidator idValidator = new IntRangeValidator(0);
    private final IntRangeValidator distanceValidator = new IntRangeValidator(1);

    @Override
    public boolean validate(Culoar value) {
        errorMessages.clear();
        boolean valid = true;

        if (!idValidator.validate((int)value.getId())) {
            errorMessages.add("id: " + idValidator.getErrorMessage());
            valid = false;
        }

        if (!distanceValidator.validate(value.getDistanta())) {
            errorMessages.add("Distance: " + distanceValidator.getErrorMessage());
            valid = false;
        }

        return valid;
    }

    public void validateThrow(Culoar value) throws ValidationException {
        if(!validate(value)){
            throw new ValidationException(getErrorMessage());
        }
    }

    @Override
    public String getErrorMessage() {
        return String.join("\n", errorMessages);
    }

}
