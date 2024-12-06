package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.AccountRecoveryModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.UsernameRecoveryDTO;
import com.srinivasa.refrigerationworks.srw.service.UserCredentialService;
import com.srinivasa.refrigerationworks.srw.utility.common.StringEditor;
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
}
