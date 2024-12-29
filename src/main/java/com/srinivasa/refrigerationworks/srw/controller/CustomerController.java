package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.CustomerModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.UserIdentifierDTO;
import com.srinivasa.refrigerationworks.srw.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * Controller that handles requests related to the Customer entity.
 */
@Controller
@RequestMapping("/SRW/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /*
     * Handles GET requests to fetch the list of all customers.
     * Adds the list of customers to the model and returns the view for displaying customer details.
     */
    @GetMapping("/list")
    public String getCustomerList(Model model) {
        CustomerModel.addCustomersToModel(customerService.getCustomerList(), model);
        return "customer/customer-list";
    }

    /*
     * Handles GET requests to fetch the list of all active customers.
     * Adds the list of active customers to the model and returns the view for displaying customer details.
     */
    @GetMapping("/active-list")
    public String getActiveCustomerList(Model model) {
        CustomerModel.addCustomersToModel(customerService.getActiveCustomerList(), model);
        return "customer/customer-list";
    }

    /*
     * Handles the POST request to search for a customer by their identifier.
     * - Validates the input and displays the customer details if no errors occur.
     */
    @PostMapping("/search")
    public String getCustomer(@ModelAttribute UserIdentifierDTO userIdentifierDTO, Model model) {
        CustomerModel.addCustomerToModel(customerService.getCustomerByIdentifier(userIdentifierDTO.getIdentifier()), model);
        return "customer/customer-details";
    }
}