package com.srinivasa.refrigerationworks.srw.validation;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/*
 * Custom annotation for validating the uniqueness of a field's value.
 * - Ensures that the value of the annotated field is unique within a specified entity or across multiple user-related entities.
 * - The annotation can be repeated multiple times on the same element using the @Repeatable annotation.
 */
@Constraint(validatedBy = UniqueValueConstraintValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(UniqueValues.class)
public @interface UniqueValue {

    /*
     * Default error message when validation fails.
     */
    public String message() default "Value already exists.";

    /*
     * Validation groups to categorize constraints.
     */
    public Class<?>[] groups() default {};

    /*
     * Payload for clients of the Bean Validation API.
     */
    public Class<? extends Payload>[] payload() default {};

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