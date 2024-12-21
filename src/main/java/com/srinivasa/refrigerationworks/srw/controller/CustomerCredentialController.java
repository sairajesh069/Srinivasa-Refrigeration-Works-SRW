package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.UserCredentialModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.CustomerCredentialDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.CustomerDTO;
import com.srinivasa.refrigerationworks.srw.service.CustomerCredentialService;
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
 * Controller for handling customer credential operations
 */
@Controller
@RequestMapping("/SRW/customer")
@RequiredArgsConstructor
public class CustomerCredentialController {

    private final CustomerCredentialService customerCredentialService;

    /*
     * Initializes the binder to trim strings
     */
    @InitBinder
    public void initialize(WebDataBinder webDataBinder) {
        StringEditor.stringTrimmer(webDataBinder);
    }

    /*
     * Confirms the customer registration and adds customer credentials
     */
    @PostMapping("/confirmation")
    public String confirmCustomer(@ModelAttribute @Valid CustomerCredentialDTO customerCredentialDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            UserCredentialModel.addUserFormConstantsToModel(model);
            return "customer/customer-register-form";
        }
        customerCredentialService.addCustomerCredential(customerCredentialDTO);
        return "customer/customer-confirmation";
    }

    /*
     * Handles POST request to update customer's details after validation.
     * Redirects to customer list on success.
     */
    @PostMapping("/update")
    public String updateCustomer(@ModelAttribute("customerDTO") @Valid CustomerDTO updatedCustomerDTO, BindingResult bindingResult, Model model, HttpSession session) {
        if(bindingResult.hasErrors()) {
            UserCredentialModel.addUserFormConstantsToModel(model);
            return "customer/customer-update-form";
        }
        customerCredentialService.updateCustomer((CustomerDTO) session.getAttribute("initialCustomerDTO"), updatedCustomerDTO);
        return "redirect:/SRW/customer/list";
    }

    /*
     * Handles the GET request to activate a customer.
     * - Activates the customer based on the provided customerId.
     * - Redirects to the customer list page upon success.
     */
    @GetMapping("/activate")
    public String activateCustomer(@RequestParam("customerId") String customerId) {
        customerCredentialService.activateCustomer(customerId);
        return "redirect:/SRW/customer/list";
    }

    /*
     * Handles the GET request to deactivate a customer.
     * - Deactivates the customer based on the provided customerId.
     * - Redirects to the customer list page upon success.
     */
    @GetMapping("/deactivate")
    public String deactivateCustomer(@RequestParam("customerId") String customerId) {
        customerCredentialService.deactivateCustomer(customerId);
        return "redirect:/SRW/customer/list";
    }
}