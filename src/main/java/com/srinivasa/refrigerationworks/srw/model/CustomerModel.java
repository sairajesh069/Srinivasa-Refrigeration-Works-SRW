package com.srinivasa.refrigerationworks.srw.model;

import com.srinivasa.refrigerationworks.srw.payload.dto.CustomerDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.UserIdentifierDTO;
import org.springframework.ui.Model;

import java.util.List;

/*
 * Contains methods for adding customer-related data to the model.
 */
public class CustomerModel {

    /*
     * Adds UserIdentifierDTO and list of customers to the model.
     * If no customers are found, adds a "noCustomersFound" message.
     */
    public static void addCustomersToModel(UserIdentifierDTO userIdentifierDTO, List<CustomerDTO> customers, Model model) {
        model.addAttribute("userIdentifierDTO", userIdentifierDTO);
        model.addAttribute(
                (customers.isEmpty() || customers.get(0) == null) ? "noCustomersFound" : "customers",
                (customers.isEmpty() || customers.get(0) == null) ? "No Customer Entries in Database" : customers);
    }

    /*
     * Adds customer details to the model.
     * If customer is null, adds a "noCustomerFound" message.
     * Otherwise, adds the customer details.
     */
    public static void addCustomerToModel(CustomerDTO customer, Model model) {
        model.addAttribute(
                customer == null ? "noCustomerFound" : "customer",
                customer == null ? "Customer not found for the given details." : customer);
    }
}