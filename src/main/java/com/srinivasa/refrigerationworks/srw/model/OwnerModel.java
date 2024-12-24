package com.srinivasa.refrigerationworks.srw.model;

import com.srinivasa.refrigerationworks.srw.payload.dto.OwnerDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.UserIdentifierDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import java.util.List;

/*
 * Contains methods for adding owner-related data to the model.
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
     * Adds a new UserIdentifierDTO object to the model for user input.
     * Also adds a flag to indicate that data should be fetched.
     */
    public static void addUserIdentifierDTOToModel(Model model) {
        model.addAttribute("userIdentifierDTO", new UserIdentifierDTO());
        addToFetchToModel(true, model);
    }

    /*
     * Adds owner details to the model.
     * - If owner is null, adds a message indicating no owner found.
     * - Otherwise, adds the owner details.
     * - Also adds a flag to indicate whether data should be fetched.
     */
    public static void addOwnerDetailsToModel(OwnerDTO owner, boolean toFetch, Model model, HttpSession session) {
        model.addAttribute(
                owner == null ? "noOwnerFound" : "owner",
                owner == null ? "Owner not found for the given details." : owner);
        addToFetchToModel(toFetch, model);
        if(!toFetch) {
            session.setAttribute("ownerDetails", owner);
        }
    }

    /*
     * Adds a flag to the model indicating whether to fetch data.
     * - `toFetch`: A boolean flag to determine if data should be fetched.
     */
    public static void addToFetchToModel(boolean toFetch, Model model) {
        model.addAttribute("toFetch", toFetch);
    }

    /*
     * Adds OwnerDTO to the model and stores the initial state in the session for update comparison.
     */
    public static void addOwnerDTOForUpdateToModel(OwnerDTO ownerDTO, Model model, HttpSession session) {
        model.addAttribute("ownerDTO", ownerDTO);
        session.setAttribute("initialOwnerDTO", ownerDTO);
    }
}