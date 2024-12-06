package com.srinivasa.refrigerationworks.srw.model;

import com.srinivasa.refrigerationworks.srw.payload.dto.UsernameRecoveryDTO;
import org.springframework.ui.Model;

public class AccountRecoveryModel {

    /*
     * Adds a new instance of UsernameRecoveryDTO to the model.
     * This is used to bind form data during username recovery.
     */
    public static void addUserCredentialDTOToModel(Model model) {
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
}
