package com.srinivasa.refrigerationworks.srw.model;

import com.srinivasa.refrigerationworks.srw.payload.dto.OwnerDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.UserIdentifierDTO;
import org.springframework.ui.Model;
import java.util.List;

/*
 * Contains methods for adding owner-related data to the model.
 * It provides functionality for adding a list of owners to the model.
 */
public class OwnerModel {

    /*
     * Adds a list of owners to the model, or a message if no owners are found.
     * The owners list is added to the model under the attribute "owners" if not empty,
     * otherwise a "noOwnersFound" message is added.
     */
    public static void addOwnerListToModel(List<OwnerDTO> owners, Model model) {
        model.addAttribute(
                owners.isEmpty() ? "noOwnersFound" : "owners",
                owners.isEmpty() ? "No Owner Entries in Database" : owners);
    }

    /*
     * Adds a new UserIdentifierDTO object to the model.
     * Used to capture user input for searching an owner.
     */
    public static void addUserIdentifierDTOToModel(Model model) {
        model.addAttribute("userIdentifierDTO", new UserIdentifierDTO());
    }

    /*
     * Adds owner details or a fallback message to the model.
     * If the owner is found, adds the OwnerDTO object; otherwise, adds an error message.
     */
    public static void addOwnerDetailsToModel(OwnerDTO owner, Model model) {
        model.addAttribute(
                owner == null ? "noOwnerFound" : "owner",
                owner == null ? "Owner not found for the given details." : owner);
    }

}