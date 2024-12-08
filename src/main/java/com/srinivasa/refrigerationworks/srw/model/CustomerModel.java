package com.srinivasa.refrigerationworks.srw.model;

import com.srinivasa.refrigerationworks.srw.payload.dto.CustomerDTO;
import org.springframework.ui.Model;

import java.util.List;

/*
 * Contains methods for adding customer-related data to the model.
 * It provides functionality for adding a list of customers to the model.
 */
public class CustomerModel {

    /*
     * Adds a list of customers to the model, or a message if no customers are found.
     * The customers list is added to the model under the attribute "customers" if not empty,
     * otherwise a "noUsersFound" message is added.
     */
    public static void addCustomerListToModel(List<CustomerDTO> customers, Model model) {
        model.addAttribute(
                customers.isEmpty() ? "noUsersFound" : "customers",
                customers.isEmpty() ? "No Customer Entries in Database" : customers);
    }
}
