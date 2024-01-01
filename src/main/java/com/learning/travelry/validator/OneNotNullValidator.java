package com.learning.travelry.validator;

import com.learning.travelry.annonations.OneNotNull;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class OneNotNullValidator implements ConstraintValidator<OneNotNull, Object> {

    @Override
    public void initialize(OneNotNull constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Let other validations handle null object
        }

        for (Field field : value.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getName().equals("id")) continue;
            try {
                if (field.get(value) != null) {
                    return true;
                }
            } catch (IllegalAccessException e) {
                System.out.println(e.getMessage());
            }
        }

        return false;
    }
}
