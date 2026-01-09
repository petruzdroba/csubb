package com.validators;

public class DoubleValidator implements Validator<Double> {
    private String errorMessage;
    private final double min;
    private final double max;

    public DoubleValidator() {
        this.min = Double.NEGATIVE_INFINITY;
        this.max = Double.POSITIVE_INFINITY;
    }

    public DoubleValidator(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean validate(Double value) {
        if (value == null) {
            errorMessage = "Value cannot be null";
            return false;
        }

        if (value < min || value > max) {
            errorMessage = "Value must be between " + min + " and " + max;
            return false;
        }

        errorMessage = null;
        return true;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
