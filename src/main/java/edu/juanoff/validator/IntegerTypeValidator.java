package edu.juanoff.validator;

public class IntegerTypeValidator implements TypeValidator {
    @Override
    public boolean isValid(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    @Override
    public String getOutputFileName() {
        return "integers.txt";
    }
}
