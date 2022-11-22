package ru.javawebinar.topjava.util;

import javax.validation.*;
import java.util.Set;

public class FieldValidator {
    private static final ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = vf.getValidator();

    public static void validate(Object object) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}
