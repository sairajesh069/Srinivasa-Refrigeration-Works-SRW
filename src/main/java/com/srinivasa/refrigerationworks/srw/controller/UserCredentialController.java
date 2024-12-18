package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.UserCredentialModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.*;
import com.srinivasa.refrigerationworks.srw.service.EmployeeService;
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
    private final EmployeeService employeeService;


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

    /*
     * Handles GET request to display employee update form with current employee data.
     */
    @GetMapping("/employee/update")
    public String updateEmployee(@RequestParam("employeeId") String employeeId, Model model, HttpSession session) {
        UserCredentialModel.addEmployeeDTOForUpdateToModel(employeeService.getEmployeeByIdentifier(employeeId), model, session);
        UserCredentialModel.addUserFormConstantsToModel(model);
        return "employee/employee-update-form";
    }

    /*
     * Handles POST request to update employee's details after validation.
     * Redirects to employee list on success.
     */
    @PostMapping("/employee/update")
    public String updateEmployee(@ModelAttribute("employeeDTO") @Valid EmployeeDTO updatedEmployeeDTO, BindingResult bindingResult, Model model, HttpSession session) {
        if(bindingResult.hasErrors()) {
            UserCredentialModel.addUserFormConstantsToModel(model);
            return "employee/employee-update-form";
        }
        userCredentialService.updateEmployee((EmployeeDTO) session.getAttribute("initialEmployeeDTO"), updatedEmployeeDTO);
        return "redirect:/SRW/employee/list";
    }
}