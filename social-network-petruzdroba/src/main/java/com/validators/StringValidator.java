package main.java.com.validators;

public class StringValidator implements Validator<String>{
    private int minLength = 0;
    private int maxLength = Integer.MAX_VALUE;
    private String errorMessage;

    public StringValidator() {}

    public StringValidator(int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public boolean validate(String value) {
        boolean valid = true;
        if(value == null) {
            errorMessage = "String cannot be null";
            valid = false;
        }

        if (value.isEmpty()) {
            errorMessage = "String cannot be empty";
            valid = false;
        }

        if (value.length() < minLength || value.length() > maxLength) {
            errorMessage = "String length must be between " + minLength + " and " + maxLength;
            valid = false;
        }

        return valid;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
}
