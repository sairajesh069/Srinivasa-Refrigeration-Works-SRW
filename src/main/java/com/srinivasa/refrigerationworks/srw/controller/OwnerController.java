package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.OwnerModel;
import com.srinivasa.refrigerationworks.srw.model.UserCredentialModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.UserIdentifierDTO;
import com.srinivasa.refrigerationworks.srw.service.OwnerService;
import com.srinivasa.refrigerationworks.srw.utility.common.SubStringExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*
 * Controller that handles requests related to the Owner entity.
 */
@Controller
@RequestMapping("/SRW/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    /*
     * Displays the owner registration form
     */
    @GetMapping("/register")
    public String createOwner(Model model) {
        UserCredentialModel.addOwnerCredentialToModel(model);
        return "owner/owner-register-form";
    }

    /*
     * Handles GET requests to fetch the list of all owners.
     * Adds the list of owners to the model and returns the view for displaying owner details.
     */
    @GetMapping("/list")
    public String getOwnerList(Model model) {
        OwnerModel.addOwnerListToModel(ownerService.getOwnerList(), model);
        return "owner/owner-list";
    }

    /*
     * Handles GET requests to fetch the list of all active owners.
     * Adds the list of active owners to the model and returns the view for displaying owner details.
     */
    @GetMapping("/active-list")
    public String getActiveOwnerList(Model model) {
        OwnerModel.addOwnerListToModel(ownerService.getActiveOwnerList(), model);
        return "owner/owner-list";
    }

    /*
     * Handles requests related to searching and displaying owner details.
     */
    @GetMapping("/search")
    public String getOwner(Model model) {
        OwnerModel.addUserIdentifierDTOToModel(model);
        return "owner/owner-details";
    }

    /*
     * Processes the owner search request.
     * Validates the user identifier and retrieves owner details.
     */
    @PostMapping("/search")
    public String getOwner(@Valid UserIdentifierDTO userIdentifierDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "owner/owner-details";
        }
        OwnerModel.addOwnerDetailsToModel(
                ownerService.getOwnerByIdentifier(userIdentifierDTO.getIdentifier()), model);
        return "owner/owner-details";
    }

    /*
     * Handles GET request to display owner update form with current owner data.
     * Sets session attribute using substring from the "Referer" header.
     */
    @GetMapping("/update")
    public String updateOwner(@RequestParam("ownerId") String ownerId, Model model, HttpSession session, HttpServletRequest request) {
        OwnerModel.addOwnerDTOForUpdateToModel(ownerService.getOwnerByIdentifier(ownerId), model, session);
        UserCredentialModel.addUserFormConstantsToModel(model);
        session.setAttribute("updateEndpointOrigin", SubStringExtractor.extractSubString(request.getHeader("Referer"), "owner/"));
        return "owner/owner-update-form";
    }
}