package edu.juanoff.validator;

public class StringTypeValidator implements TypeValidator {
    @Override
    public boolean isValid(String value) {
        return value != null && !value.isEmpty();
    }

    @Override
    public String getOutputFileName() {
        return "strings.txt";
    }
}
