package com.srinivasa.refrigerationworks.srw.model;

import com.srinivasa.refrigerationworks.srw.payload.dto.CustomerDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.UserIdentifierDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.List;

/*
 * Contains methods for adding customer-related data to the model.
 */
public class CustomerModel {

    /*
     * Adds UserIdentifierDTO to the model.
     * Adds a list of customers to the model, or a message if no customers are found.
     * The customers list is added to the model under the attribute "customers" if not empty,
     * otherwise a "noCustomersFound" message is added.
     */
    public static void addCustomerListToModel(List<CustomerDTO> customers, Model model) {
        model.addAttribute("userIdentifierDTO", new UserIdentifierDTO());
        model.addAttribute(
                customers.isEmpty() ? "noCustomersFound" : "customers",
                customers.isEmpty() ? "No Customer Entries in Database" : customers);
    }

    /*
     * Adds customer details to the model.
     * - If customer is null, adds a message indicating no customer found.
     * - Otherwise, adds the customer details.
     */
    public static void addCustomerDetailsToModel(CustomerDTO customer, Model model, HttpSession session) {
        model.addAttribute(
                customer == null ? "noCustomerFound" : "customer",
                customer == null ? "Customer not found for the given details." : customer);
        session.setAttribute("customerDetails", customer);
    }

    /*
     * Adds CustomerDTO to the model and stores the initial state in the session for update comparison.
     */
    public static void addCustomerDTOForUpdateToModel(CustomerDTO customerDTO, Model model, HttpSession session) {
        model.addAttribute("customerDTO", customerDTO);
        session.setAttribute("initialCustomerDTO", customerDTO);
    }
}