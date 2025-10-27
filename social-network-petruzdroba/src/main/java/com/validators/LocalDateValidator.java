package main.java.com.validators;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LocalDateValidator implements Validator<LocalDate> {
    private List<String> errorMessages = new ArrayList<>();
    private final LocalDate minDate;
    private final LocalDate maxDate;

    public LocalDateValidator() {
        this.minDate = null;
        this.maxDate = null;
    }

    public LocalDateValidator(LocalDate minDate, LocalDate maxDate) {
        this.minDate = minDate;
        this.maxDate = maxDate;
    }

    @Override
    public boolean validate(LocalDate value) {
        errorMessages.clear(); // reset errors
        boolean valid = true;

        if (value == null) {
            errorMessages.add("Date cannot be null");
            return false; // can't check anything else if null
        }

        if (minDate != null && value.isBefore(minDate)) {
            errorMessages.add("Date cannot be before " + minDate);
            valid = false;
        }

        if (maxDate != null && value.isAfter(maxDate)) {
            errorMessages.add("Date cannot be after " + maxDate);
            valid = false;
        }

        int day = value.getDayOfMonth();
        int month = value.getMonthValue();
        if (day < 1 || day > 31) {
            errorMessages.add("Invalid day: " + day);
            valid = false;
        }
        if (month < 1 || month > 12) {
            errorMessages.add("Invalid month: " + month);
            valid = false;
        }

        return valid;
    }

    @Override
    public String getErrorMessage() {
        return String.join("\n", errorMessages);
    }
}
