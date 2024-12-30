package com.srinivasa.refrigerationworks.srw.model;

import com.srinivasa.refrigerationworks.srw.payload.dto.PasswordResetDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.UsernameRecoveryDTO;
import org.springframework.ui.Model;

/*
 * Handles adding account-recovery data to the model.
 */
public class AccountRecoveryModel {

    /*
     * Adds UsernameRecoveryDTO to the model.
     */
    public static void addUsernameRecoveryDTOToModel(Model model) {
        model.addAttribute("usernameRecoveryDTO", new UsernameRecoveryDTO());
    }

    /*
     * Adds username or error message to the model.
     */
    public static void usernameRecoveryModel(String username, Model model) {
        boolean isUsernameEmpty = username == null || username.isEmpty();
        model.addAttribute(
                isUsernameEmpty ? "errorMessage" : "username",
                isUsernameEmpty ? "No user found with the provided phone number." : username);
    }

    /*
     * Adds PasswordResetDTO and user validation state to the model.
     */
    public static void addPasswordResetDTOToModel(Model model) {
        model.addAttribute("passwordResetDTO", new PasswordResetDTO());
        addUserNotValidatedToModel(model);
    }

    /*
     * Adds a flag indicating user is not validated.
     */
    public static void addUserNotValidatedToModel(Model model) {
        model.addAttribute("isUserValidated", false);
    }

    /*
     * Adds validation feedback and updates validation status.
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
     * Adds success message after password update and resets validation state.
     */
    public static void addPasswordUpdatedToModel(Model model) {
        model.addAttribute("validationSuccess", "");
        model.addAttribute("passwordUpdated", "Your password has been successfully updated.");
        addUserNotValidatedToModel(model);
    }
}