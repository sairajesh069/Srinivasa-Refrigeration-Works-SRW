package com.srinivasa.refrigerationworks.srw.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/*
 * Custom annotation for validating the uniqueness of a field's value.
 * - Ensures the annotated field's value is unique within a specified entity or across user-related entities.
 */
@Constraint(validatedBy = UniqueValueConstraintValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(UniqueValues.class)
public @interface UniqueValue {

    /*
     * Default error message when validation fails.
     */
    String message() default "Value already exists.";

    /*
     * Validation groups to categorize constraints.
     */
    Class<?>[] groups() default {};

    /*
     * Payload for clients of the Bean Validation API.
     */
    Class<? extends Payload>[] payload() default {};

    /*
     * The name of the field to check for uniqueness.
     */
    String fieldName();

    /*
     * The entity class containing the field.
     */
    Class<?> entityClass() default Void.class;

    /*
     * Determines if uniqueness is checked across multiple user entities.
     */
    boolean inEveryUserEntity() default true;

    /*
     * The user identifier field used in comparison.
     */
    String userIdField();
}