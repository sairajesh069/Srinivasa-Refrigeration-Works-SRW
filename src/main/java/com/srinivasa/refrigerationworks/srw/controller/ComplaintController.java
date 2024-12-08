package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.ComplaintModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.ComplaintDTO;
import com.srinivasa.refrigerationworks.srw.service.ComplaintService;
import com.srinivasa.refrigerationworks.srw.utility.common.StringEditor;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

/*
 * Controller class to handle complaint-related operations like registering a complaint and updating dropdowns.
 * It manages the interactions with the ComplaintService and handles form submissions for complaints.
 */
@Controller
@RequestMapping("/SRW/complaint")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;

    /*
     * Initializes data binder to trim input strings.
     */
    @InitBinder
    public void initialize(WebDataBinder webDataBinder) {
        StringEditor.stringTrimmer(webDataBinder);
    }

    /*
     * Displays the complaint registration form.
     */
    @GetMapping("/register")
    public String bookRepair(Model model) {
        ComplaintModel.addComplaintDTOToModel(model); // Adds empty ComplaintDTO object to the model
        return "complaint/complaint-register-form"; // Returns the view for the complaint registration form
    }

    /*
     * Handles form submission for registering a complaint.
     * Validates the complaint data, processes the complaint registration,
     * and redirects to the confirmation page if successful.
     * If validation fails, it re-displays the registration form with error messages.
     */
    @PostMapping("/confirmation")
    public String registerComplaint(@ModelAttribute @Valid ComplaintDTO complaintDTO, BindingResult bindingResult, Model model, Principal principal) {
        if(bindingResult.hasErrors()) { // If there are validation errors
            if(complaintDTO.getProductType() != null) { // Populate dropdowns based on the selected product type
                ComplaintModel.populateDropDownsForProduct(complaintDTO.getProductType(), model);
            }
            else {
                ComplaintModel.addProductTypesToModel(model); // Adds available product types if none selected
            }
            return "complaint/complaint-register-form"; // Return the complaint registration form view with errors
        }
        complaintService.registerComplaint(complaintDTO, principal.getName()); // Register the complaint using the service
        return "complaint/complaint-confirmation"; // Return the complaint confirmation view
    }

    /*
     * Updates dropdown options based on selected product type.
     */
    @PostMapping("/update-dropdown")
    public String bookRepair(ComplaintDTO complaintDTO, Model model) {
        ComplaintModel.populateDropDownsForProduct(complaintDTO.getProductType(), model); // Updates dropdowns based on product type
        return "complaint/complaint-register-form"; // Returns the complaint registration form view with updated dropdowns
    }
}