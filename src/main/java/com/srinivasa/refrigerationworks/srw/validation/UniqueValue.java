package com.srinivasa.refrigerationworks.srw.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/*
 * Custom annotation for unique value validation.
 * - Ensures the annotated field's value is unique in the database.
 * - fieldName: Specifies the field to check for uniqueness.
 * - entityClass: Specifies the entity to query.
 * - inEveryUserEntity: Determines if uniqueness is checked across multiple user-related entities.
 */
@Constraint(validatedBy = UniqueValueConstraintValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueValue {

    /*
     * Error message when validation fails.
     */
    public String message() default "Value already exists.";

    /*
     * Validation groups.
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
     * The entity class where the field resides.
     */
    Class<?> entityClass();

    /*
     * Indicates whether the field should be unique across multiple user entities.
     */
    boolean inEveryUserEntity();

}