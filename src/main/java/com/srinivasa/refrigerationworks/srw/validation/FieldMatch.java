package com.srinivasa.refrigerationworks.srw.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/*
 * Custom annotation for validating that two fields match.
 * - firstField: The first field to compare.
 * - secondField: The second field to compare.
 * - message: The default error message when fields don't match.
 * - groups: Specifies validation groups.
 * - payload: Carries additional metadata.
 */
@Constraint(validatedBy = FieldMatchConstraintValidator.class) /* Links to the custom validator */
@Target({ElementType.TYPE}) /* Can be applied to classes or objects */
@Retention(RetentionPolicy.RUNTIME) /* Available at runtime for validation */
public @interface FieldMatch {

    public String message() default "Fields did not match.";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

    String firstField();

    String secondField();
}
