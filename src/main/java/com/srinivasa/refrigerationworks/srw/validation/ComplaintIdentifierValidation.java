package com.srinivasa.refrigerationworks.srw.validation;

import com.srinivasa.refrigerationworks.srw.payload.dto.ComplaintIdentifierDTO;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;

/*
 * A utility class for validating the complaint identifier (complaint ID or phone number) and associated registered date.
 * Ensures that necessary fields are present based on the identifier type.
 */
public class ComplaintIdentifierValidation {

    /*
     * Validates the complaint identifier (either complaint ID or phone number) and checks if the required registered date is provided.
     * If the identifier is empty or invalid, an error is added.
     * If the identifier is a phone number (10-digit), the registered date must also be provided; otherwise, an error is added.
     */
    public static void identifierValidation(ComplaintIdentifierDTO complaintIdentifier, BindingResult bindingResult) {

        String identifier = complaintIdentifier.getIdentifier();
        LocalDate date = complaintIdentifier.getRegisteredDate();

        /* If the identifier is empty, it's like asking for a recipe but forgetting to mention the key ingredient. */
        if(identifier == null || identifier.isEmpty()) {
            bindingResult.rejectValue("identifier", "error.phoneNumber", "Invalid input: Please provide a valid complaint ID or phone number");
        }

        /* If the identifier is a phone number, but there's no registered date, it's like asking for an appointment but not mentioning the date. */
        else if(identifier.matches("\\d{10}") && date == null) {
            bindingResult.rejectValue("registeredDate", "error.registeredDate", "Registered date is required when fetching details using a phone number");
        }
    }
}