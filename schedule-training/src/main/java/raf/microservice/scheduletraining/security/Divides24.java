package raf.microservice.scheduletraining.security;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import raf.microservice.scheduletraining.security.service.impl.Divides24Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Divides24Validator.class)
public @interface Divides24 {

    String message() default "24 must be divisible by the number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}