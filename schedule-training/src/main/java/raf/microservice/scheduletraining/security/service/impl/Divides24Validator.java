package raf.microservice.scheduletraining.security.service.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import raf.microservice.scheduletraining.security.Divides24;

public class Divides24Validator implements ConstraintValidator<Divides24, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value != null && value>0 && 24 % value == 0;
    }
}