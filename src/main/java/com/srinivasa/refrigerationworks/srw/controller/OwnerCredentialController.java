package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.OwnerModel;
import com.srinivasa.refrigerationworks.srw.model.UserCredentialModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.OwnerCredentialDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.OwnerDTO;
import com.srinivasa.refrigerationworks.srw.service.OwnerCredentialService;
import com.srinivasa.refrigerationworks.srw.utility.common.StringEditor;
import com.srinivasa.refrigerationworks.srw.utility.common.SubStringExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/*
 * Controller for handling owner credential operations
 */
@Controller
@RequestMapping("/SRW/owner")
@RequiredArgsConstructor
public class OwnerCredentialController {

    /*
     * Service for handling operations related to owner credentials.
     */
    private final OwnerCredentialService ownerCredentialService;

    /*
     * Initializes the binder to trim strings
     */
    @InitBinder
    public void initialize(WebDataBinder webDataBinder) {
        StringEditor.stringTrimmer(webDataBinder);
    }

    /*
     * Displays the owner registration form
     */
    @GetMapping("/register")
    public String createOwner(Model model) {
        UserCredentialModel.addOwnerCredentialToModel(model);
        return "owner/owner-register-form";
    }

    /*
     * Confirms the owner registration and adds owner credentials.
     * If validation fails, re-displays the form with error messages.
     */
    @PostMapping("/confirmation")
    public String confirmOwner(@ModelAttribute @Valid OwnerCredentialDTO ownerCredentialDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            UserCredentialModel.addUserFormConstantsToModel(model);
            return "owner/owner-register-form";
        }
        ownerCredentialService.addOwnerCredential(ownerCredentialDTO);
        return "owner/owner-confirmation";
    }

    /*
     * Handles GET request to display owner update form with current owner data.
     * Sets session attribute using substring from the "Referer" header.
     */
    @GetMapping("/update")
    public String updateOwner(@RequestParam("ownerId") String ownerId, Model model, HttpServletRequest request) {
        OwnerModel.addOwnerToModel(ownerCredentialService.getOwnerById(ownerId), model);
        UserCredentialModel.addUserFormConstantsToModel(model);
        UserCredentialModel.addUpdateEndpointOriginToModel(
                SubStringExtractor.extractSubString(request.getHeader("Referer"), "owner/"), model);
        return "owner/owner-update-form";
    }

    /*
     * Handles POST request to update owner's details after validation.
     * Redirects to origin owner page of update endpoint on success.
     */
    @PostMapping("/update")
    public String updateOwner(@ModelAttribute("owner") @Valid OwnerDTO updatedOwnerDTO, BindingResult bindingResult,
                              @RequestParam("updateEndpointOrigin") String updateEndpointOrigin, Model model) {
        if(bindingResult.hasErrors()) {
            UserCredentialModel.addUserFormConstantsToModel(model);
            UserCredentialModel.addUpdateEndpointOriginToModel(updateEndpointOrigin, model);
            return "owner/owner-update-form";
        }
        ownerCredentialService.updateOwner(ownerCredentialService.getOwnerById(updatedOwnerDTO.getOwnerId()), updatedOwnerDTO);
        return "redirect:/SRW/owner/" + updateEndpointOrigin;
    }

    /*
     * Handles the GET request to activate an owner.
     * - Activates the owner based on the provided ownerId.
     * - Redirects to the originating owner page on success.
     */
    @GetMapping("/activate")
    public String activateOwner(@RequestParam("ownerId") String ownerId, HttpServletRequest request) {
        ownerCredentialService.activateOwner(ownerId);
        return "redirect:/SRW/owner/" + SubStringExtractor.extractSubString(request.getHeader("Referer"), "owner/");
    }

    /*
     * Handles the GET request to deactivate an owner.
     * - Deactivates the owner based on the provided ownerId.
     * - Redirects to the owner list page upon success.
     */
    @GetMapping("/deactivate")
    public String deactivateOwner(@RequestParam("ownerId") String ownerId, HttpServletRequest request) {
        ownerCredentialService.deactivateOwner(ownerId);
        return "redirect:/SRW/owner/" + SubStringExtractor.extractSubString(request.getHeader("Referer"), "owner/");
    }
}