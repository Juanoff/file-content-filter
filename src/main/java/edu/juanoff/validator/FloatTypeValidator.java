package edu.juanoff.validator;

import edu.juanoff.OutputFileName;

public class FloatTypeValidator implements TypeValidator {
    @Override
    public boolean isValid(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(value.trim());
            return value.contains(".");
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    @Override
    public String getOutputFileName() {
        return OutputFileName.FLOATS.getFileName();
    }
}
