package edu.juanoff.validator;

import edu.juanoff.OutputFileName;

import java.util.List;

public class StringTypeValidator implements TypeValidator {
    private final List<TypeValidator> otherValidators = List.of(
            new IntegerTypeValidator(),
            new FloatTypeValidator()
    );

    @Override
    public boolean isValid(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        return otherValidators.stream().noneMatch(validator -> validator.isValid(value));
    }

    @Override
    public String getOutputFileName() {
        return OutputFileName.STRINGS.getFileName();
    }
}
