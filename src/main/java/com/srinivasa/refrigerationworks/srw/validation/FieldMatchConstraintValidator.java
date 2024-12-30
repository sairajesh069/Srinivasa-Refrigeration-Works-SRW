package com.srinivasa.refrigerationworks.srw.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

/*
 * Custom validator to ensure two fields match in an object.
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
     * message: The error message when fields do not match.
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
         * Return true if value is null (no validation required)
         */
        if (value == null) {
            return true;
        }

        /*
         * Use reflection to get the field values
         */
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
        String firstValue = (String) beanWrapper.getPropertyValue(firstField);
        String secondValue = (String) beanWrapper.getPropertyValue(secondField);

        /* Return true if values match */
        if (firstValue != null && firstValue.equals(secondValue)) {
            return true;
        }

        /*
         * If values don't match, set a custom error message
         */
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(secondField)
                .addConstraintViolation();
        return false;
    }
}