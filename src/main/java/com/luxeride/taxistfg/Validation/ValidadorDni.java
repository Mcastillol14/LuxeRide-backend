package com.luxeride.taxistfg.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidadorDni implements ConstraintValidator<DniValido, String> {

    @Override
    public boolean isValid(String dni, ConstraintValidatorContext context) {
        return dni != null && dni.matches("\\d{8}[A-HJ-NP-TV-Z]");
    }
}
    