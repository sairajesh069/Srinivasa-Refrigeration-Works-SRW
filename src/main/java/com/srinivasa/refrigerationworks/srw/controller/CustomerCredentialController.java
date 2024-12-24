package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.CustomerModel;
import com.srinivasa.refrigerationworks.srw.model.UserCredentialModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.CustomerCredentialDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.CustomerDTO;
import com.srinivasa.refrigerationworks.srw.service.CustomerCredentialService;
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

import java.security.Principal;

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
     * Redirects to origin customer page of update endpoint on success.
     */
    @PostMapping("/update")
    public String updateCustomer(@ModelAttribute("customerDTO") @Valid CustomerDTO updatedCustomerDTO, BindingResult bindingResult, Model model, HttpSession session) {
        if(bindingResult.hasErrors()) {
            UserCredentialModel.addUserFormConstantsToModel(model);
            return "customer/customer-update-form";
        }
        customerCredentialService.updateCustomer((CustomerDTO) session.getAttribute("initialCustomerDTO"), updatedCustomerDTO);
        return "redirect:/SRW/customer/" + session.getAttribute("updateEndpointOrigin");
    }

    /*
     * Handles the GET request to activate a customer.
     * - Activates the customer based on the provided customerId.
     * - Redirects to the originating customer page upon success.
     */
    @GetMapping("/activate")
    public String activateCustomer(@RequestParam("customerId") String customerId, HttpServletRequest request) {
        customerCredentialService.activateCustomer(customerId);
        return "redirect:/SRW/customer/" + SubStringExtractor.extractSubString(request.getHeader("Referer"), "customer/");
    }

    /*
     * Handles the GET request to deactivate a customer.
     * - Deactivates the customer based on the provided customerId.
     * - Redirects to the originating customer page upon success.
     */
    @GetMapping("/deactivate")
    public String deactivateCustomer(@RequestParam("customerId") String customerId, HttpServletRequest request) {
        customerCredentialService.deactivateCustomer(customerId);
        return "redirect:/SRW/customer/" + SubStringExtractor.extractSubString(request.getHeader("Referer"), "customer/");
    }

    /*
     * Handles the GET request to fetch the logged-in customer's profile.
     * - Retrieves the customer details using the username from the Principal object.
     */
    @GetMapping("/my-profile")
    public String getCustomerProfile(Model model, Principal principal, HttpSession session) {
        CustomerModel.addCustomerDetailsToModel(customerCredentialService.getCustomerByUsername(principal.getName()), false, model, session);
        return "customer/customer-details";
    }
}