package com.srinivasa.refrigerationworks.srw.model;

import com.srinivasa.refrigerationworks.srw.payload.dto.OwnerDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.UserIdentifierDTO;
import org.springframework.ui.Model;

import java.util.List;

/*
 * Contains methods for adding owner-related data to the model.
 */
public class OwnerModel {

    /*
     * Adds UserIdentifierDTO and list of owners to the model.
     * If no owners are found, adds a "noOwnersFound" message.
     */
    public static void addOwnersToModel(UserIdentifierDTO userIdentifierDTO, List<OwnerDTO> owners, Model model) {
        model.addAttribute("userIdentifierDTO", userIdentifierDTO);
        model.addAttribute(
                (owners.isEmpty() || owners.get(0) == null) ? "noOwnersFound" : "owners",
                (owners.isEmpty() || owners.get(0) == null) ? "No Owner Entries in Database" : owners);
    }

    /*
     * Adds owner details to the model.
     * If owner is null, adds a "noOwnerFound" message.
     * Otherwise, adds the owner details.
     */
    public static void addOwnerToModel(OwnerDTO owner, Model model) {
        model.addAttribute(
                owner == null ? "noOwnerFound" : "owner",
                owner == null ? "Owner not found for the given details." : owner);
    }
}