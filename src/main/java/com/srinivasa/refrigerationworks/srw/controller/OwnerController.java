package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.OwnerModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.UserIdentifierDTO;
import com.srinivasa.refrigerationworks.srw.service.OwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * Controller that handles requests related to the Owner entity.
 * It provides a mapping to fetch and display the list of owners in the owner-list view.
 */
@Controller
@RequestMapping("/SRW/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

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
}
