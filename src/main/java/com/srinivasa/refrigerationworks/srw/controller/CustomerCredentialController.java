package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.CustomerModel;
import com.srinivasa.refrigerationworks.srw.model.UserCredentialModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.CustomerCredentialDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.CustomerDTO;
import com.srinivasa.refrigerationworks.srw.service.CustomerCredentialService;
import com.srinivasa.refrigerationworks.srw.utility.UserRoleProvider;
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
     * Displays the customer registration form
     */
    @GetMapping("/register")
    public String createCustomer(Model model) {
        UserCredentialModel.addCustomerCredentialToModel(model);
        return "customer/customer-register-form";
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
     * Handles GET request to display customer update form with current customer data.
     * Sets session attribute using substring from the "Referer" header.
     */
    @GetMapping("/update")
    public String updateCustomer(@RequestParam("customerId") String customerId, Model model,
                                 HttpSession session, HttpServletRequest request) {
        String refererEndpoint = SubStringExtractor.extractSubString(request.getHeader("Referer"), "customer/");
        if(UserRoleProvider.fetchUserRole(session).equals("ROLE_OWNER") || refererEndpoint.equals("my-profile")) {
            CustomerModel.addCustomerToModel(customerCredentialService.getCustomerById(customerId), model);
            UserCredentialModel.addUserFormConstantsToModel(model);
            UserCredentialModel.addUpdateEndpointOriginToModel(refererEndpoint, model);
            return "customer/customer-update-form";
        }
        return "access-denied";
    }

    /*
     * Handles POST request to update customer's details after validation.
     * Redirects to origin customer page of update endpoint on success.
     */
    @PostMapping("/update")
    public String updateCustomer(@ModelAttribute("customer") @Valid CustomerDTO updatedCustomerDTO, BindingResult bindingResult,
                                 @RequestParam("updateEndpointOrigin") String updateEndpointOrigin, Model model) {
        if(bindingResult.hasErrors()) {
            UserCredentialModel.addUserFormConstantsToModel(model);
            UserCredentialModel.addUpdateEndpointOriginToModel(updateEndpointOrigin, model);
            return "customer/customer-update-form";
        }
        customerCredentialService.updateCustomer(customerCredentialService.getCustomerById(updatedCustomerDTO.getCustomerId()), updatedCustomerDTO);
        return "redirect:/SRW/customer/" + updateEndpointOrigin;
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
     */
    @GetMapping("/my-profile")
    public String getCustomerProfile(Model model, HttpSession session) {
        CustomerModel.addCustomerToModel(customerCredentialService.getCustomerById((String) session.getAttribute("userId")), model);
        return "customer/customer-details";
    }
}