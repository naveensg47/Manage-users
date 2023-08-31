package com.naveen.learning.validation.validator;

import com.naveen.learning.validation.annotation.NullOrNotBlank;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, String> {
    @Override
    public void initialize(NullOrNotBlank parameters) {
        //ConstraintValidator.super.initialize(parameters);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (null == value) {
            return true;
        }
        if (value.length() == 0) {
            return false;
        }
        boolean isAllWhiteSpace=value.matches("\\s*$");
        return !isAllWhiteSpace;
    }
}
