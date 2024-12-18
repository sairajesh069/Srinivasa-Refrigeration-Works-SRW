package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.UserCredentialModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.CustomerCredentialDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.EmployeeCredentialDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.OwnerCredentialDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.OwnerDTO;
import com.srinivasa.refrigerationworks.srw.service.OwnerService;
import com.srinivasa.refrigerationworks.srw.service.UserCredentialService;
import com.srinivasa.refrigerationworks.srw.utility.common.StringEditor;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/*
 * Controller for handling user credential operations
 */
@Controller
@RequestMapping("/SRW")
@RequiredArgsConstructor
public class UserCredentialController {

    private final UserCredentialService userCredentialService;

    private final OwnerService ownerService;

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
    @GetMapping("/owner/register")
    public String createOwner(Model model) {
        UserCredentialModel.addOwnerCredentialToModel(model);
        return "owner/owner-register-form";
    }

    /*
     * Confirms the owner registration and adds owner credentials
     */
    @PostMapping("/owner/confirmation")
    public String confirmOwner(@ModelAttribute @Valid OwnerCredentialDTO ownerCredentialDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            UserCredentialModel.addUserFormConstantsToModel(model);
            return "owner/owner-register-form";
        }
        userCredentialService.addOwnerCredential(ownerCredentialDTO);
        return "owner/owner-confirmation";
    }

    /*
     * Displays the employee registration form
     */
    @GetMapping("/employee/register")
    public String createEmployee(Model model) {
        UserCredentialModel.addEmployeeCredentialToModel(model);
        return "employee/employee-register-form";
    }

    /*
     * Confirms the employee registration and adds employee credentials
     */
    @PostMapping("/employee/confirmation")
    public String confirmEmployee(@ModelAttribute @Valid EmployeeCredentialDTO employeeCredentialDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            UserCredentialModel.addUserFormConstantsToModel(model);
            return "employee/employee-register-form";
        }
        userCredentialService.addEmployeeCredential(employeeCredentialDTO);
        return "employee/employee-confirmation";
    }

    /*
     * Displays the customer registration form
     */
    @GetMapping("/customer/register")
    public String createCustomer(Model model) {
        UserCredentialModel.addCustomerCredentialToModel(model);
        return "customer/customer-register-form";
    }

    /*
     * Confirms the customer registration and adds customer credentials
     */
    @PostMapping("/customer/confirmation")
    public String confirmCustomer(@ModelAttribute @Valid CustomerCredentialDTO customerCredentialDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            UserCredentialModel.addUserFormConstantsToModel(model);
            return "customer/customer-register-form";
        }
        userCredentialService.addCustomerCredential(customerCredentialDTO);
        return "customer/customer-confirmation";
    }

    /*
     * Handles GET request to display owner update form with current owner data.
     */
    @GetMapping("/owner/update")
    public String updateOwner(@RequestParam("ownerId") String ownerId, Model model, HttpSession session) {
        UserCredentialModel.addOwnerDTOForUpdateToModel(ownerService.getOwnerByIdentifier(ownerId), model, session);
        UserCredentialModel.addUserFormConstantsToModel(model);
        return "owner/owner-update-form";
    }

    /*
     * Handles POST request to update owner's details after validation.
     * Redirects to owner list on success.
     */
    @PostMapping("/owner/update")
    public String updateOwner(@ModelAttribute("ownerDTO") @Valid OwnerDTO updatedOwnerDTO, BindingResult bindingResult, Model model, HttpSession session) {
        if(bindingResult.hasErrors()) {
            UserCredentialModel.addUserFormConstantsToModel(model);
            return "owner/owner-update-form";
        }
        userCredentialService.updateOwner((OwnerDTO) session.getAttribute("initialOwnerDTO"), updatedOwnerDTO);
        return "redirect:/SRW/owner/list";
    }
}