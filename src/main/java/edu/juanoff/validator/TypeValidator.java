package edu.juanoff.validator;

public interface TypeValidator {
    boolean isValid(String value);

    String getOutputFileName();
}
