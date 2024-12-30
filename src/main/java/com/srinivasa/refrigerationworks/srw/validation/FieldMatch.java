package com.srinivasa.refrigerationworks.srw.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * Custom annotation to validate that two fields match.
 */
@Constraint(validatedBy = FieldMatchConstraintValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldMatch {

    /*
     * message: Default error message when fields don't match.
     */
    public String message() default "Fields did not match.";

    /*
     * groups: Specifies validation groups.
     */
    public Class<?>[] groups() default {};

    /*
     * payload: Carries additional metadata.
     */
    public Class<? extends Payload>[] payload() default {};

    /*
     * firstField: The first field to compare.
     */
    String firstField();

    /*
     * secondField: The second field to compare.
     */
    String secondField();
}