package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.UserCredentialModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.*;
import com.srinivasa.refrigerationworks.srw.service.CustomerCredentialService;
import com.srinivasa.refrigerationworks.srw.service.EmployeeCredentialService;
import com.srinivasa.refrigerationworks.srw.service.OwnerCredentialService;
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

    private final OwnerCredentialService ownerCredentialService;
    private final EmployeeCredentialService employeeCredentialService;
    private final CustomerCredentialService customerCredentialService;

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
    @PostMapping("/owner/confirmation")
    public String confirmOwner(@ModelAttribute @Valid OwnerCredentialDTO ownerCredentialDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            UserCredentialModel.addUserFormConstantsToModel(model);
            return "owner/owner-register-form";
        }
        ownerCredentialService.addOwnerCredential(ownerCredentialDTO);
        return "owner/owner-confirmation";
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
        employeeCredentialService.addEmployeeCredential(employeeCredentialDTO);
        return "employee/employee-confirmation";
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
        customerCredentialService.addCustomerCredential(customerCredentialDTO);
        return "customer/customer-confirmation";
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
        ownerCredentialService.updateOwner((OwnerDTO) session.getAttribute("initialOwnerDTO"), updatedOwnerDTO);
        return "redirect:/SRW/owner/list";
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
        employeeCredentialService.updateEmployee((EmployeeDTO) session.getAttribute("initialEmployeeDTO"), updatedEmployeeDTO);
        return "redirect:/SRW/employee/list";
    }

    /*
     * Handles POST request to update customer's details after validation.
     * Redirects to customer list on success.
     */
    @PostMapping("/customer/update")
    public String updateCustomer(@ModelAttribute("customerDTO") @Valid CustomerDTO updatedCustomerDTO, BindingResult bindingResult, Model model, HttpSession session) {
        if(bindingResult.hasErrors()) {
            UserCredentialModel.addUserFormConstantsToModel(model);
            return "customer/customer-update-form";
        }
        customerCredentialService.updateCustomer((CustomerDTO) session.getAttribute("initialCustomerDTO"), updatedCustomerDTO);
        return "redirect:/SRW/customer/list";
    }

    /*
     * Handles the GET request to deactivate an owner.
     * - Deactivates the owner based on the provided ownerId.
     * - Redirects to the owner list page upon success.
     */
    @GetMapping("/owner/deactivate")
    public String deactivateOwner(@RequestParam("ownerId") String ownerId) {
        ownerCredentialService.deactivateOwner(ownerId);
        return "redirect:/SRW/owner/list";
    }

    /*
     * Handles the GET request to deactivate an employee.
     * - Deactivates the employee based on the provided employeeId.
     * - Redirects to the employee list page upon success.
     */
    @GetMapping("/employee/deactivate")
    public String deactivateEmployee(@RequestParam("employeeId") String employeeId) {
        employeeCredentialService.deactivateEmployee(employeeId);
        return "redirect:/SRW/employee/list";
    }

    /*
     * Handles the GET request to deactivate a customer.
     * - Deactivates the customer based on the provided customerId.
     * - Redirects to the customer list page upon success.
     */
    @GetMapping("/customer/deactivate")
    public String deactivateCustomer(@RequestParam("customerId") String customerId) {
        customerCredentialService.deactivateCustomer(customerId);
        return "redirect:/SRW/customer/list";
    }
}