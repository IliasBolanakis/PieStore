package com.ilbolan.piesstore.forms.annotations;


import com.ilbolan.piesstore.forms.annotations.validators.TelephoneValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Telephone constrains annotation based on greek standards
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TelephoneValidator.class)
public @interface TelephoneConstrains {

    String message() default "Λάθος μορφή τηλεφώνου";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
