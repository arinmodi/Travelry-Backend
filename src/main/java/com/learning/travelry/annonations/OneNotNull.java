package com.learning.travelry.annonations;

import com.learning.travelry.validator.OneNotNullValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OneNotNullValidator.class)
@Documented
public @interface OneNotNull {
    String message() default "Exactly one of the fields must be set and the other must be null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Fields to validate against null.
     */
    String[] fields() default {};
}
