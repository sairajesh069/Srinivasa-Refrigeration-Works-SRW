package com.srinivasa.refrigerationworks.srw.validation;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/*
 * Custom validator to check if two fields match in an object.
 */
@Component
public class FieldMatchConstraintValidator implements ConstraintValidator<FieldMatch, Object> {

    /*
     * firstField: The first field to compare.
     */
    private String firstField;

    /*
     * secondField: The second field to compare.
     */
    private String secondField;

    /*
     * message: The error message to show when fields do not match.
     */
    private String message;

    @Override
    public void initialize(FieldMatch fieldMatch) {
        this.firstField = fieldMatch.firstField();
        this.secondField = fieldMatch.secondField();
        this.message = fieldMatch.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        /*
         * If value is null, validation is not needed
         */
        if (value==null) {
            return true;
        }

        /* Create BeanWrapper for reflection
         * Get values from the first and second fields
         */
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
        String firstValue = (String) beanWrapper.getPropertyValue(firstField);
        String secondValue = (String) beanWrapper.getPropertyValue(secondField);

        /* Return true if values match */
        if(firstValue != null && firstValue.equals(secondValue)) {
            return true;
        }

        /* Disable default error message
         * Build custom error message and attach to second field
         * Return false
         */
        else{
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(secondField)
                    .addConstraintViolation();
            return false;
        }
    }
}