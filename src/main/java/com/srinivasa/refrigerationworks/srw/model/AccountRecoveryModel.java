package com.srinivasa.refrigerationworks.srw.model;

import com.srinivasa.refrigerationworks.srw.payload.dto.PasswordResetDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.UsernameRecoveryDTO;
import org.springframework.ui.Model;

/*
 * Contains methods for adding account-recovery related data to the model.
 */
public class AccountRecoveryModel {

    /*
     * Adds a new instance of UsernameRecoveryDTO to the model.
     * This is used to bind form data during username recovery.
     */
    public static void addUsernameRecoveryDTOToModel(Model model) {
        model.addAttribute("usernameRecoveryDTO", new UsernameRecoveryDTO());
    }

    /*
     * Populates the model with the username or an error message.
     * - If the username is found, it is added to the model.
     * - If not, an error message is added instead.
     */
    public static void usernameRecoveryModel(String username, Model model) {
        boolean isUsernameEmpty = username == null || username.isEmpty();
        model.addAttribute(
                isUsernameEmpty ? "errorMessage" : "username",
                isUsernameEmpty ? "No user found with the provided phone number." : username);
    }

    /*
     * Adds a new PasswordResetDTO instance to the model for form binding.
     * - Also adds the default user validation state to the model.
     */
    public static void addPasswordResetDTOToModel(Model model) {
        model.addAttribute("passwordResetDTO", new PasswordResetDTO());
        addUserNotValidatedToModel(model);
    }

    /*
     * Adds a flag to the model indicating the user is not validated.
     */
    public static void addUserNotValidatedToModel(Model model) {
        model.addAttribute("isUserValidated", false);
    }

    /*
     * Adds validation feedback to the model.
     * - Displays a success message if validation succeeds.
     * - Displays an error message if validation fails.
     * - Updates the model with the validation status.
     */
    public static void addIsUserValidatedToModel(boolean isUserValidated, Model model) {
        model.addAttribute(
                isUserValidated ? "validationSuccess" : "errorMessage",
                isUserValidated
                        ? "You have been successfully validated. Please set your new password."
                        : "No user matches the provided details."
        );
        model.addAttribute("isUserValidated", isUserValidated);
    }

    /*
     * Adds a success message to the model after the password is updated.
     * - Resets the user validation state.
     */
    public static void addPasswordUpdatedToModel(Model model) {
        model.addAttribute("validationSuccess", "");
        model.addAttribute("passwordUpdated", "Your password has been successfully updated.");
        addUserNotValidatedToModel(model);
    }
}