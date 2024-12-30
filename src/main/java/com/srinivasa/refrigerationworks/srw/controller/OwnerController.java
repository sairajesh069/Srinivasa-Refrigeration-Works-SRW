package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.OwnerModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.UserIdentifierDTO;
import com.srinivasa.refrigerationworks.srw.service.OwnerService;
import com.srinivasa.refrigerationworks.srw.utility.common.SubStringExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * Controller that handles requests related to the Owner entity.
 */
@Controller
@RequestMapping("/SRW/owner")
@RequiredArgsConstructor
public class OwnerController {

    /*
     * Service for handling operations related to owner entities.
     */
    private final OwnerService ownerService;

    /*
     * Handles GET requests to fetch the list of all owners.
     * Adds the list of owners to the model and returns the view for displaying owner details.
     */
    @GetMapping("/list")
    public String getOwnerList(Model model) {
        OwnerModel.addOwnersToModel(ownerService.getOwnerList(), model);
        return "owner/owner-list";
    }

    /*
     * Handles GET requests to fetch the list of all active owners.
     * Adds the list of active owners to the model and returns the view for displaying owner details.
     */
    @GetMapping("/active-list")
    public String getActiveOwnerList(Model model) {
        OwnerModel.addOwnersToModel(ownerService.getActiveOwnerList(), model);
        return "owner/owner-list";
    }

    /*
     * Handles the POST request to search for an owner by their identifier.
     * - Validates the input and displays the owner details if no errors occur.
     */
    @PostMapping("/search")
    public String getOwner(@ModelAttribute UserIdentifierDTO userIdentifierDTO, Model model, HttpServletRequest request) {
        if(userIdentifierDTO.getIdentifier() != null) {
            OwnerModel.addOwnerToModel(ownerService.getOwnerByIdentifier(userIdentifierDTO.getIdentifier()), model);
            return "owner/owner-details";
        }
        else {
            return "redirect:/SRW/owner/" + SubStringExtractor.extractSubString(request.getHeader("Referer"), "owner/");
        }
    }

    /*
     * Handles the GET request to fetch the logged-in owner's profile.
     */
    @GetMapping("/my-profile")
    public String getOwnerProfile(Model model, HttpSession session) {
        OwnerModel.addOwnerToModel(ownerService.getOwnerByIdentifier(session.getAttribute("userId").toString()), model);
        return "owner/owner-details";
    }
}