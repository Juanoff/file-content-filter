package edu.juanoff.validator;

import edu.juanoff.OutputFileName;

public class IntegerTypeValidator implements TypeValidator {
    @Override
    public boolean isValid(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        try {
            Long.parseLong(value.trim());
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    @Override
    public String getOutputFileName() {
        return OutputFileName.INTEGERS.getFileName();
    }
}
