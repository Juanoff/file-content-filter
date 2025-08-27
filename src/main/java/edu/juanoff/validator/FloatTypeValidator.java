package edu.juanoff.validator;

public class FloatTypeValidator implements TypeValidator {
    @Override
    public boolean isValid(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    @Override
    public String getOutputFileName() {
        return "floats.txt";
    }
}
