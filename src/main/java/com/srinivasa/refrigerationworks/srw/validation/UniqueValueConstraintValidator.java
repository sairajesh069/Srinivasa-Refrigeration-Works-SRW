package com.srinivasa.refrigerationworks.srw.validation;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/*
 * Validator for the @UniqueValue annotation.
 * - Ensures the annotated field's value is unique in the database.
 */
@Component
public class UniqueValueConstraintValidator implements ConstraintValidator<UniqueValue, Object> {

    /*
     * Field name to check for uniqueness.
     */
    private String fieldName;

    /*
     * Entity class where the field resides.
     */
    private Class<?> entityClass;

    /*
     * EntityManager for database queries.
     */
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void initialize(UniqueValue uniqueValue) {
        this.fieldName = uniqueValue.fieldName();
        this.entityClass = uniqueValue.entityClass();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        /*
         * Skip validation if value is null.
         */
        if (value == null) {
            return true;
        }

        /*
         * Normalize phone numbers to include country code.
         */
        if (fieldName.equals("phoneNumber")) {
            value = value.toString().startsWith("+91") ? value : "+91" + value;
        }

        /*
         * Query for checking uniqueness in an entity.
         */
        String query = "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e WHERE e." + fieldName + " = :value";

        /*
         * Execute query and return true if no matching records are found.
         */
        Long count = entityManager
                .createQuery(query, Long.class)
                .setParameter("value", value)
                .getSingleResult();

        return count == 0;
    }
}