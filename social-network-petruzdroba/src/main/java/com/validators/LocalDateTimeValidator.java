package com.validators;

import java.time.LocalDateTime;

public class LocalDateTimeValidator implements Validator<LocalDateTime> {

    private String errorMessage;

    private final boolean allowPast;
    private final boolean allowFuture;

    public LocalDateTimeValidator() {
        this(true, true);
    }

    public LocalDateTimeValidator(boolean allowPast, boolean allowFuture) {
        this.allowPast = allowPast;
        this.allowFuture = allowFuture;
    }

    @Override
    public boolean validate(LocalDateTime value) {
        if (value == null) {
            errorMessage = "Date/time cannot be null";
            return false;
        }

        LocalDateTime now = LocalDateTime.now();

        if (!allowPast && value.isBefore(now)) {
            errorMessage = "Date/time cannot be in the past";
            return false;
        }

        if (!allowFuture && value.isAfter(now)) {
            errorMessage = "Date/time cannot be in the future";
            return false;
        }

        return true;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
