package ru.javawebinar.topjava.repository.jdbc;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class JdbcRepository {
    protected ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
    protected Validator validator = vf.getValidator();

    public static void validate(Object object, Validator validator) {
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
