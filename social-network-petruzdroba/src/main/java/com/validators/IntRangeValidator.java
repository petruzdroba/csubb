package main.java.com.validators;

public class IntRangeValidator implements Validator<Integer>{
    private final int min;
    private final int max;
    private String errorMessage;

    public IntRangeValidator(int min) {
        this.min = min;
        this.max = Integer.MAX_VALUE;
    }

    public IntRangeValidator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean validate(Integer value) {
        boolean valid = true;

        if (value == null) {
            errorMessage = "Value cannot be null";
            valid = false;
        }

        if (value < min || value > max) {
            errorMessage = "Value must be between " + min + " and " + max;
            valid = false;
        }

        return valid;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
}
