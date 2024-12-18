package com.srinivasa.refrigerationworks.srw.validation;

import com.srinivasa.refrigerationworks.srw.entity.Customer;
import com.srinivasa.refrigerationworks.srw.entity.Employee;
import com.srinivasa.refrigerationworks.srw.entity.Owner;
import com.srinivasa.refrigerationworks.srw.repository.CustomerRepository;
import com.srinivasa.refrigerationworks.srw.repository.EmployeeRepository;
import com.srinivasa.refrigerationworks.srw.repository.OwnerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

/*
 * Validator implementation for the @UniqueValue annotation.
 * Validates that a specified field's value is unique either within a single entity
 * or across multiple user-related entities (Owner, Employee, Customer).
 */
@Component
@RequiredArgsConstructor
public class UniqueValueConstraintValidator implements ConstraintValidator<UniqueValue, Object> {

    /*
     * Field name to validate for uniqueness.
     */
    private String fieldName;

    /*
     * Entity class containing the field to validate.
     */
    private Class<?> entityClass;

    /*
     * Flag indicating if the field should be unique across all user-related entities.
     */
    private boolean inEveryUserEntity;

    /*
     * Name of the user ID field for comparison during updates.
     */
    private String userIdField;

    /*
     * Custom error message for validation failures.
     */
    private String message;

    /*
     * EntityManager for dynamic query execution.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /*
     * Repositories for user-related entities for cross-entity uniqueness checks.
     */
    private final OwnerRepository ownerRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void initialize(UniqueValue uniqueValue) {
        this.fieldName = uniqueValue.fieldName();
        this.entityClass = uniqueValue.entityClass();
        this.inEveryUserEntity = uniqueValue.inEveryUserEntity();
        this.userIdField = uniqueValue.userIdField();
        this.message = uniqueValue.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        /*
         * Access field values using BeanWrapperImpl.
         */
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
        String userIdValue = (String) beanWrapper.getPropertyValue(userIdField);
        String fieldValue = (String) beanWrapper.getPropertyValue(fieldName);

        /*
         * Skip validation if field is null.
         */
        if (fieldValue == null) {
            return true;
        }

        /*
         * Normalize phone numbers by appending country code if necessary.
         */
        fieldValue = fieldValue.matches("\\d{10}") ? "+91" + fieldValue : fieldValue;

        boolean isUnique = false;

        if (inEveryUserEntity) {
            /*
             * Validate uniqueness across Owner, Employee, and Customer entities.
             */
            boolean isEntityNull = false;

            Customer customer = customerRepository.findByIdentifier(fieldValue);
            isEntityNull = customer == null;
            if (customer == null || customer.getCustomerId().equals(userIdValue)) {
                isUnique = true;
            }

            if (isEntityNull) {
                Employee employee = employeeRepository.findByIdentifier(fieldValue);
                isEntityNull = employee == null;
                if (!(employee == null || employee.getEmployeeId().equals(userIdValue))) {
                    isUnique = false;
                }
            }

            if (isEntityNull) {
                Owner owner = ownerRepository.findByIdentifier(fieldValue);
                if (!(owner == null || owner.getOwnerId().equals(userIdValue))) {
                    isUnique = false;
                }
            }
        } else {
            /*
             * Validate uniqueness within the specified entity class.
             */
            String query = "SELECT CASE WHEN COUNT(e) > 0 THEN e." + userIdField + " ELSE NULL END " +
                    "FROM " + entityClass.getSimpleName() + " e WHERE e." + fieldName + " = :value";

            /*
             * Execute query to determine uniqueness.
             */
            String entityId = entityManager
                    .createQuery(query, String.class)
                    .setParameter("value", fieldValue)
                    .getSingleResult();

            isUnique = (entityId == null || entityId.equals(userIdValue));
        }

        if (!isUnique) {
            /*
             * Add custom validation error message if the field value is not unique.
             */
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(fieldName)
                    .addConstraintViolation();
        }
        return isUnique;
    }
}