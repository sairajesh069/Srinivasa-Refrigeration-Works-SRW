package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.CustomerModel;
import com.srinivasa.refrigerationworks.srw.model.UserCredentialModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.CustomerDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.UserIdentifierDTO;
import com.srinivasa.refrigerationworks.srw.service.CustomerService;
import com.srinivasa.refrigerationworks.srw.utility.UserRoleProvider;
import com.srinivasa.refrigerationworks.srw.utility.common.SubStringExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/*
 * Controller that handles requests related to the Customer entity.
 */
@Controller
@RequestMapping("/SRW/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /*
     * Displays the customer registration form
     */
    @GetMapping("/register")
    public String createCustomer(Model model) {
        UserCredentialModel.addCustomerCredentialToModel(model);
        return "customer/customer-register-form";
    }

    /*
     * Handles GET requests to fetch the list of all customers.
     * Adds the list of customers to the model and returns the view for displaying customer details.
     */
    @GetMapping("/list")
    public String getCustomerList(Model model) {
        CustomerModel.addCustomerListToModel(customerService.getCustomerList(), model);
        return "customer/customer-list";
    }

    /*
     * Handles GET requests to fetch the list of all active customers.
     * Adds the list of active customers to the model and returns the view for displaying customer details.
     */
    @GetMapping("/active-list")
    public String getActiveCustomerList(Model model) {
        CustomerModel.addCustomerListToModel(customerService.getActiveCustomerList(), model);
        return "customer/customer-list";
    }

    /*
     * Handles the POST request to search for a customer by their identifier.
     * - Validates the input and displays the customer details if no errors occur.
     */
    @PostMapping("/search")
    public String getCustomer(@ModelAttribute @Valid UserIdentifierDTO userIdentifierDTO, BindingResult bindingResult, Model model, HttpSession session, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "redirect:/SRW/customer/" + SubStringExtractor.extractSubString(request.getHeader("Referer"), "customer/");
        }
        CustomerModel.addCustomerDetailsToModel(
                customerService.getCustomerByIdentifier(userIdentifierDTO.getIdentifier()), model, session);
        return "customer/customer-details";
    }

    /*
     * Handles GET request to display customer update form with current customer data.
     * Sets session attribute using substring from the "Referer" header.
     */
    @GetMapping("/update")
    public String updateCustomer(@RequestParam("customerId") String customerId, Model model, HttpSession session, HttpServletRequest request) {
        String refererEndpoint = SubStringExtractor.extractSubString(request.getHeader("Referer"), "customer/");
        if(UserRoleProvider.fetchUserRole(session).equals("ROLE_OWNER") || refererEndpoint.equals("my-profile")) {
            CustomerModel.addCustomerDTOForUpdateToModel(
                    refererEndpoint.equals("my-profile") ? (CustomerDTO) session.getAttribute("customerDetails") : customerService.getCustomerByIdentifier(customerId),
                    model, session);
            UserCredentialModel.addUserFormConstantsToModel(model);
            session.setAttribute("updateEndpointOrigin", refererEndpoint);
            return "customer/customer-update-form";
        }
        return "access-denied";
    }
}