package com.validators;

public interface Validator<T> {
    boolean validate(T value);

    String getErrorMessage();
}
