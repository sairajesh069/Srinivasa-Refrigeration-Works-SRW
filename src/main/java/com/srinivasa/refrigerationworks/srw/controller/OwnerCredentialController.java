package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.UserCredentialModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.OwnerCredentialDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.OwnerDTO;
import com.srinivasa.refrigerationworks.srw.service.OwnerCredentialService;
import com.srinivasa.refrigerationworks.srw.utility.common.StringEditor;
import com.srinivasa.refrigerationworks.srw.utility.common.SubStringExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

    private final OwnerCredentialService ownerCredentialService;

    /*
     * Initializes the binder to trim strings
     */
    @InitBinder
    public void initialize(WebDataBinder webDataBinder) {
        StringEditor.stringTrimmer(webDataBinder);
    }

    /*
     * Confirms the owner registration and adds owner credentials
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
     * Handles POST request to update owner's details after validation.
     * Redirects to origin owner page of update endpoint on success.
     */
    @PostMapping("/update")
    public String updateOwner(@ModelAttribute("ownerDTO") @Valid OwnerDTO updatedOwnerDTO, BindingResult bindingResult, Model model, HttpSession session) {
        if(bindingResult.hasErrors()) {
            UserCredentialModel.addUserFormConstantsToModel(model);
            return "owner/owner-update-form";
        }
        ownerCredentialService.updateOwner((OwnerDTO) session.getAttribute("initialOwnerDTO"), updatedOwnerDTO);
        return "redirect:/SRW/owner/" + session.getAttribute("updateEndpointOrigin");
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