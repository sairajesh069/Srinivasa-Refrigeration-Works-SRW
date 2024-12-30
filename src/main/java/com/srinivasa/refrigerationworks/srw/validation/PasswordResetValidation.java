package com.srinivasa.refrigerationworks.srw.validation;

import com.srinivasa.refrigerationworks.srw.payload.dto.PasswordResetDTO;
import org.springframework.validation.BindingResult;

/*
 * Validates user details and password fields in PasswordResetDTO.
 */
public class PasswordResetValidation {

    /*
     * Validates phone number and username in PasswordResetDTO.
     */
    public static void userDetailsValidation(PasswordResetDTO passwordResetDTO, BindingResult bindingResult) {

        /*
         * Validate phone number (10 digits)
         */
        if (passwordResetDTO.getPhoneNumber() == null || !passwordResetDTO.getPhoneNumber().matches("\\d{10}")) {
            bindingResult.rejectValue("phoneNumber", "error.phoneNumber", "Enter a valid 10-digit phone number.");
        }

        /*
         * Validate username (min 6 characters)
         */
        if (passwordResetDTO.getUsername() == null || passwordResetDTO.getUsername().length() < 6) {
            bindingResult.rejectValue("username", "error.username", "Enter a valid username.");
        }
    }

    /*
     * Validates password and confirmPassword in PasswordResetDTO.
     */
    public static void passwordValidation(PasswordResetDTO passwordResetDTO, BindingResult bindingResult) {

        /*
         * Validate password (min 8 characters, alphanumeric and special characters allowed)
         */
        if (passwordResetDTO.getPassword() == null || !passwordResetDTO.getPassword().matches("^[a-zA-Z0-9@.#$&_]{8,}$")) {
            bindingResult.rejectValue("password", "error.password", "Password must be at least 8 characters long and include valid characters.");
        }

        /*
         * Validate confirmPassword (must match password)
         */
        if (passwordResetDTO.getConfirmPassword() == null || !passwordResetDTO.getConfirmPassword().equals(passwordResetDTO.getPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Passwords don't match.");
        }
    }
}