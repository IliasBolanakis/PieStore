package com.ilbolan.piesstore.forms.annotations.validators;

import com.ilbolan.piesstore.forms.annotations.OrderTimeConstrains;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Validator for OrderTimeConstrains annotation
 */
public class OrderTimeValidator implements ConstraintValidator<OrderTimeConstrains, LocalDateTime> {
    @Override
    public void initialize(OrderTimeConstrains constraintAnnotation) {}

    @Override
    public boolean isValid(LocalDateTime orderTime, ConstraintValidatorContext constraintValidatorContext) {
        // set ordering time-window 12:00 - 22:00
        return orderTime.isAfter(
                        LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 0,0,0)))
                && orderTime.isBefore(
                        LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 0,0,0)));
    }
}
