package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.OwnerModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.UserIdentifierDTO;
import com.srinivasa.refrigerationworks.srw.service.OwnerService;
import com.srinivasa.refrigerationworks.srw.utility.common.EndpointExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;

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
        UserIdentifierDTO userIdentifierDTO = (UserIdentifierDTO) model.getAttribute("userIdentifierDTO");
        OwnerModel.addOwnersToModel(userIdentifierDTO == null ? new UserIdentifierDTO() : userIdentifierDTO,
                ownerService.getOwnerList(), model);
        return "owner/owner-list";
    }

    /*
     * Handles GET requests to fetch the list of all active owners.
     * Adds the list of active owners to the model and returns the view for displaying owner details.
     */
    @GetMapping("/active-list")
    public String getActiveOwnerList(Model model) {
        UserIdentifierDTO userIdentifierDTO = (UserIdentifierDTO) model.getAttribute("userIdentifierDTO");
        OwnerModel.addOwnersToModel(userIdentifierDTO == null ? new UserIdentifierDTO() : userIdentifierDTO,
                ownerService.getActiveOwnerList(), model);
        return "owner/owner-list";
    }

    /*
     * Handles the POST request to search for an owner by their identifier.
     * - Validates the input and displays the owner details if no errors occur.
     */
    @PostMapping("/search")
    public String getOwner(@ModelAttribute @Valid UserIdentifierDTO userIdentifierDTO, BindingResult bindingResult,
                           Model model, HttpSession session, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String refererEndpoint = EndpointExtractor.ownerEndpoint(request);
        String searchEndpointOrigin = refererEndpoint.equals("search") ? (String) session.getAttribute("searchEndpointOrigin") : refererEndpoint;
        String identifier = userIdentifierDTO.getIdentifier();
        if(!bindingResult.hasErrors() && !identifier.isEmpty()) {
            OwnerModel.addOwnersToModel(userIdentifierDTO, Collections.singletonList(ownerService.getOwnerByIdentifier(identifier)), model);
            session.setAttribute("searchEndpointOrigin", searchEndpointOrigin);
            return "owner/owner-list";
        }
        redirectAttributes.addFlashAttribute("userIdentifierDTO", userIdentifierDTO);
        return "redirect:/SRW/owner/" + searchEndpointOrigin;
    }

    /*
     * Handles the GET request to fetch the logged-in owner's profile.
     */
    @GetMapping("/my-profile")
    public String getOwnerProfile(Model model, HttpSession session) {
        OwnerModel.addOwnerToModel(ownerService.getOwnerByIdentifier((String) session.getAttribute("userId")), model);
        return "owner/owner-details";
    }
}