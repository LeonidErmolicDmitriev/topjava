package ru.javawebinar.topjava.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class FieldValidator {
    private static final ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = vf.getValidator();

    public static void validate(Object object) {
        Set<ConstraintViolation<Object>> constraintViolations = validator
                .validate(object);
        StringBuilder errors = new StringBuilder();
        for (ConstraintViolation<Object> cv : constraintViolations) {
            errors.append(String.format(
                    "Внимание, ошибка! property: [%s], value: [%s], message: [%s]",
                    cv.getPropertyPath(), cv.getInvalidValue(), cv.getMessage()));
            errors.append("\n");
        }
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(errors.toString());
        }
    }
}
