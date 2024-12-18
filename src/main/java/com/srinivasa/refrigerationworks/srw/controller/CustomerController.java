package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.CustomerModel;
import com.srinivasa.refrigerationworks.srw.model.UserCredentialModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.UserIdentifierDTO;
import com.srinivasa.refrigerationworks.srw.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
     * Handles requests related to searching and displaying customer details.
     */
    @GetMapping("/search")
    public String getCustomer(Model model) {
        CustomerModel.addUserIdentifierDTOToModel(model);
        return "customer/customer-details";
    }

    /*
     * Processes the customer search request.
     * Validates the user identifier and retrieves customer details.
     */
    @PostMapping("/search")
    public String getCustomer(@Valid UserIdentifierDTO userIdentifierDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "customer/customer-details";
        }
        CustomerModel.addCustomerDetailsToModel(
                customerService.getCustomerByIdentifier(userIdentifierDTO.getIdentifier()), model);
        return "customer/customer-details";
    }

    /*
     * Handles GET request to display customer update form with current customer data.
     */
    @GetMapping("/update")
    public String updateCustomer(@RequestParam("customerId") String customerId, Model model, HttpSession session) {
        CustomerModel.addCustomerDTOForUpdateToModel(customerService.getCustomerByIdentifier(customerId), model, session);
        UserCredentialModel.addUserFormConstantsToModel(model);
        return "customer/customer-update-form";
    }
}
