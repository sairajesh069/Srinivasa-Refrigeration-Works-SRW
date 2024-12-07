package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.AccountRecoveryModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.PasswordResetDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.UsernameRecoveryDTO;
import com.srinivasa.refrigerationworks.srw.service.UserCredentialService;
import com.srinivasa.refrigerationworks.srw.utility.common.StringEditor;
import com.srinivasa.refrigerationworks.srw.validation.PasswordResetValidation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * Controller for handling account recovery operations.
 * Provides endpoints for username recovery.
 */
@Controller
@RequestMapping("/SRW")
@RequiredArgsConstructor
public class AccountRecoveryController {

    /*
     * Service for interacting with user credentials data.
     */
    private final UserCredentialService userCredentialService;

    /*
     * Customizes data binding by trimming string inputs.
     */
    @InitBinder
    public void initialize(WebDataBinder webDataBinder) {
        StringEditor.stringTrimmer(webDataBinder);
    }

    /*
     * Displays the username recovery page.
     * - Adds necessary DTOs to the model.
     */
    @GetMapping("/username-recovery")
    public String getUsername(Model model) {
        AccountRecoveryModel.addUserCredentialDTOToModel(model);
        return "account-recovery/username-recovery";
    }

    /*
     * Handles username recovery requests.
     * - Validates input data and checks for errors.
     * - Fetches the username if inputs are valid and updates the model.
     */
    @PostMapping("/username-recovery")
    public String fetchUsername(@Valid UsernameRecoveryDTO usernameRecoveryDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "account-recovery/username-recovery";
        }
        AccountRecoveryModel.usernameRecoveryModel(userCredentialService.fetchUsername(usernameRecoveryDTO), model);
        return "account-recovery/username-recovery";
    }

    /*
     * Handles GET requests for the password reset page.
     * - Adds a PasswordResetDTO instance to the model.
     * - Returns the password reset view.
     */
    @GetMapping("/password-reset")
    public String resetPassword(Model model) {
        AccountRecoveryModel.addPasswordResetDTOToModel(model);
        return "account-recovery/password-reset";
    }

    /*
     * Handles the POST request for password reset validation and update.
     * Validates user details and updates the password if validation succeeds.
     */
    @PostMapping("/password-reset")
    public String validateUser(PasswordResetDTO passwordResetDTO, BindingResult bindingResult, Model model) {

        /*
         * Perform custom validation on user details.
         */
        PasswordResetValidation.userDetailsValidation(passwordResetDTO, bindingResult);

        /*
         * Check if there are validation errors in user details.
         * If errors exist, return the password reset view with errors displayed.
         */
        if (bindingResult.hasErrors()) {
            AccountRecoveryModel.addUserNotValidatedToModel(model);
            return "account-recovery/password-reset";
        }

        /*
         * Validate if the user exists with the provided details.
         * If validation fails, add error messages to the model.
         */
        if (!userCredentialService.validateUser(passwordResetDTO)) {
            AccountRecoveryModel.addIsUserValidatedToModel(false, model);
        } else {
            /*
             * If user validation succeeds, proceed to password validation.
             */
            AccountRecoveryModel.addIsUserValidatedToModel(true, model);
            PasswordResetValidation.passwordValidation(passwordResetDTO, bindingResult);

            /*
             * If password validation fails, return the password reset view with errors.
             */
            if (bindingResult.hasErrors()) {
                return "account-recovery/password-reset";
            }

            /*
             * Update the user's password if all validations pass.
             */
            userCredentialService.updatePassword(passwordResetDTO);
            AccountRecoveryModel.addPasswordUpdatedToModel(model);
        }

        /*
         * Return the password reset view with appropriate success or error messages.
         */
        return "account-recovery/password-reset";
    }

}
