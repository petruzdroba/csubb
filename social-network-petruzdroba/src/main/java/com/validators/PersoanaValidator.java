package main.java.com.validators;

import main.java.com.domain.Duck;
import main.java.com.domain.Persoana;
import main.java.com.exceptions.ValidationException;

public class PersoanaValidator extends UserValidator<Persoana>{
    private final StringValidator numeValidator = new StringValidator(1,50);
    private final StringValidator prenumeValidator = new StringValidator(1,50);
    private final LocalDateValidator dataNasteriiValidator = new LocalDateValidator();
    private final StringValidator ocupatieValidator = new StringValidator(0,20);
    private final IntRangeValidator nivelEmpatieValidator = new IntRangeValidator(0,10);


    @Override
    public boolean validate(Persoana value) {
        errorMessages.clear();
        boolean valid = true;

        if(!super.validate(value)){
            valid = false;
            for(String error: super.getErrorMessage().split("\n"))
                errorMessages.add(error);
        }

        if (!numeValidator.validate(value.getNume())) {
            errorMessages.add("Nume: " + numeValidator.getErrorMessage());
            valid = false;
        }

        if (!prenumeValidator.validate(value.getPrenume())) {
            errorMessages.add("Prenume: " + prenumeValidator.getErrorMessage());
            valid = false;
        }

        if (!dataNasteriiValidator.validate(value.getDataNasterii())) {
            errorMessages.add("Data Nasterii: " + dataNasteriiValidator.getErrorMessage());
            valid = false;
        }

        if (!ocupatieValidator.validate(value.getOcupatie())) {
            errorMessages.add("Ocupatie: " + ocupatieValidator.getErrorMessage());
            valid = false;
        }

        if (!nivelEmpatieValidator.validate(value.getNivelEmpatie())) {
            errorMessages.add("Nivel Empatie: " + nivelEmpatieValidator.getErrorMessage());
            valid = false;
        }

        return valid;
    }

    public void validateThrow(Persoana value) throws ValidationException {
        if(!validate(value)){
            throw new ValidationException(getErrorMessage());
        }
    }

    @Override
    public String getErrorMessage() {
        return String.join("\n", errorMessages);
    }
}
