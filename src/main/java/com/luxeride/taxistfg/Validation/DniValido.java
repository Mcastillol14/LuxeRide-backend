package com.luxeride.taxistfg.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidadorDni.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DniValido {
    String message() default "DNI inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

