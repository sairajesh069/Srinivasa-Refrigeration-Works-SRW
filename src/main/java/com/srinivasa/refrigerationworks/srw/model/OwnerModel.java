package com.srinivasa.refrigerationworks.srw.model;

import com.srinivasa.refrigerationworks.srw.payload.dto.OwnerDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.UserIdentifierDTO;
import com.srinivasa.refrigerationworks.srw.utility.common.SubStringExtractor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;

import java.util.List;

/*
 * Contains methods for adding owner-related data to the model.
 */
public class OwnerModel {

    /*
     * Adds UserCredentialDTO to the model.
     * Adds a list of owners to the model, or a message if no owners are found.
     * The owners list is added to the model under the attribute "owners" if not empty,
     * otherwise a "noOwnersFound" message is added.
     */
    public static void addOwnersToModel(List<OwnerDTO> owners, Model model) {
        model.addAttribute("userIdentifierDTO", new UserIdentifierDTO());
        model.addAttribute(
                owners.isEmpty() ? "noOwnersFound" : "owners",
                owners.isEmpty() ? "No Owner Entries in Database" : owners);
    }

    /*
     * Adds owner details to the model.
     * - If owner is null, adds a message indicating no owner found.
     * - Otherwise, adds the owner details.
     */
    public static void addOwnerToModel(OwnerDTO owner, Model model) {
        model.addAttribute(
                owner == null ? "noOwnerFound" : "owner",
                owner == null ? "Owner not found for the given details." : owner);
    }
}