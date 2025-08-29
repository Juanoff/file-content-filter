package edu.juanoff.validator;

import edu.juanoff.OutputFileName;

import java.math.BigDecimal;

public class FloatTypeValidator implements TypeValidator {
    private final TypeValidator integerValidator = new IntegerTypeValidator();
    private static final BigDecimal MAX_FLOAT = new BigDecimal(String.valueOf(Double.MAX_VALUE));
    private static final int MAX_SIGNIFICANT_DIGITS = 17;

    @Override
    public boolean isValid(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        try {
            double doubleValue = Double.parseDouble(value.trim());
            if (Double.isInfinite(doubleValue) || Double.isNaN(doubleValue)) {
                return false;
            }

            BigDecimal val = new BigDecimal(value.trim());
            if (val.abs().compareTo(MAX_FLOAT) > 0) {
                return false;
            }

            int significantDigits = val.stripTrailingZeros().precision();
            if (significantDigits > MAX_SIGNIFICANT_DIGITS) {
                return false;
            }
            return !integerValidator.isValid(value);
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    @Override
    public String getOutputFileName() {
        return OutputFileName.FLOATS.getFileName();
    }
}
