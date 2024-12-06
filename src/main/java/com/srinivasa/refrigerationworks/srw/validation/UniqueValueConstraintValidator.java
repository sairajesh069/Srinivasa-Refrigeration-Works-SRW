package com.srinivasa.refrigerationworks.srw.validation;

import com.srinivasa.refrigerationworks.srw.repository.CustomerRepository;
import com.srinivasa.refrigerationworks.srw.repository.EmployeeRepository;
import com.srinivasa.refrigerationworks.srw.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/*
 * Validator for the @UniqueValue annotation.
 * Ensures that the annotated field's value is unique within the specified entity or across multiple user-related entities.
 */
@Component
@RequiredArgsConstructor
public class UniqueValueConstraintValidator implements ConstraintValidator<UniqueValue, Object> {

    /*
     * Name of the field to check for uniqueness.
     */
    private String fieldName;

    /*
     * Entity class containing the field.
     */
    private Class<?> entityClass;

    /*
     * Flag to determine if the field should be unique across all user-related entities.
     */
    private boolean inEveryUserEntity;

    /*
     * EntityManager used for executing dynamic queries.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /*
     * Repositories for user-related entities to support multi-entity uniqueness checks.
     */
    private final OwnerRepository ownerRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void initialize(UniqueValue uniqueValue) {
        this.fieldName = uniqueValue.fieldName();
        this.entityClass = uniqueValue.entityClass();
        this.inEveryUserEntity = uniqueValue.inEveryUserEntity();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        /*
         * Skip validation if the field value is null.
         */
        if (value == null) {
            return true;
        }

        /*
         * Normalize phone number by appending the country code if missing.
         */
        if (fieldName.equals("phoneNumber")) {
            value = value.toString().startsWith("+91") ? value : "+91" + value;
        }

        /*
         * Holds the result of the uniqueness check:
         * - True if the value is unique (does not exist in the specified entities).
         * - False if the value already exists.
         */
        boolean isUnique = false;

        if (inEveryUserEntity) {
            /*
             * Check uniqueness across Owner, Employee, and Customer entities.
             */
            if (fieldName.equals("phoneNumber")) {
                isUnique = !(ownerRepository.existsByPhoneNumber((String) value)
                        || employeeRepository.existsByPhoneNumber((String) value)
                        || customerRepository.existsByPhoneNumber((String) value));
            } else if (fieldName.equals("email")) {
                isUnique = !(ownerRepository.existsByEmail((String) value)
                        || employeeRepository.existsByEmail((String) value)
                        || customerRepository.existsByEmail((String) value));
            }
        } else {
            /*
             * Check uniqueness within a single specified entity.
             */
            String query = "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e WHERE e." + fieldName + " = :value";

            /*
             * Execute query and determine if the value is unique.
             */
            Long count = entityManager
                    .createQuery(query, Long.class)
                    .setParameter("value", value)
                    .getSingleResult();

            isUnique = count == 0;
        }

        return isUnique;
    }
}