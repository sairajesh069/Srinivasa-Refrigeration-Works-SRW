package com.srinivasa.refrigerationworks.srw.validation;

import com.srinivasa.refrigerationworks.srw.payload.dto.PasswordResetDTO;
import org.springframework.validation.BindingResult;

/*
 * Provides validation logic for user details and password reset fields in PasswordResetDTO.
 */
public class PasswordResetValidation {

    /*
     * Validates user details (phone number and username) from PasswordResetDTO.
     * - Checks if the phone number is a valid 10-digit number.
     * - Checks if the username is at least 6 characters long.
     */
    public static void userDetailsValidation(PasswordResetDTO passwordResetDTO, BindingResult bindingResult) {

        /*
         * Validate phone number:
         * - Ensure it is not null, not empty, and matches a 10-digit pattern.
         */
        String phoneNumber = passwordResetDTO.getPhoneNumber();
        if (phoneNumber == null || phoneNumber.isEmpty() || !phoneNumber.matches("\\d{10}")) {
            bindingResult.rejectValue("phoneNumber", "error.phoneNumber", "Enter a valid 10-digit phone number.");
        }

        /*
         * Validate username:
         * - Ensure it is not null and has a minimum length of 6 characters.
         */
        String username = passwordResetDTO.getUsername();
        if (username == null || username.length() < 6) {
            bindingResult.rejectValue("username", "error.username", "Enter a valid username.");
        }
    }

    /*
     * Validates password fields (password and confirmPassword) from PasswordResetDTO.
     * - Checks if the password meets the minimum requirements.
     * - Ensures confirmPassword matches the password.
     */
    public static void passwordValidation(PasswordResetDTO passwordResetDTO, BindingResult bindingResult) {

        /*
         * Validate password:
         * - Ensure it is not null, not empty, and matches the required pattern.
         */
        String password = passwordResetDTO.getPassword();
        if (password == null) {
            bindingResult.rejectValue("password", "error.password", "Set up your new password");
        } else if (password.isEmpty() || !password.matches("^[a-zA-Z0-9@.#$&_]{8,}$")) {
            bindingResult.rejectValue("password", "error.password", "Password must be at least 8 characters long and can include digits, letters, and special characters (. @ # $ & _) only");
        }

        /*
         * Validate confirmPassword:
         * - Ensure it is not null, not empty, and matches the password.
         */
        String confirmPassword = passwordResetDTO.getConfirmPassword();
        if (confirmPassword == null) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Confirm that the passwords match");
        } else if (confirmPassword.isEmpty() || !confirmPassword.equals(password)) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "The passwords don't match. Please try again.");
        }
    }
}