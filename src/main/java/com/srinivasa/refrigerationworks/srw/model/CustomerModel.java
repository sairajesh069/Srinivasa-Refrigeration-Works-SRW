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
     * Adds a list of customers to the model, or a message if no customers are found.
     * The customers list is added to the model under the attribute "customers" if not empty,
     * otherwise a "noCustomersFound" message is added.
     */
    public static void addCustomerListToModel(List<CustomerDTO> customers, Model model) {
        model.addAttribute(
                customers.isEmpty() ? "noCustomersFound" : "customers",
                customers.isEmpty() ? "No Customer Entries in Database" : customers);
    }

    /*
     * Adds a new UserIdentifierDTO object to the model for user input.
     * Also adds a flag to indicate that data should be fetched.
     */
    public static void addUserIdentifierDTOToModel(Model model) {
        model.addAttribute("userIdentifierDTO", new UserIdentifierDTO());
        addToFetchToModel(true, model);
    }

    /*
     * Adds customer details to the model.
     * - If customer is null, adds a message indicating no customer found.
     * - Otherwise, adds the customer details.
     * - Also adds a flag to indicate whether data should be fetched.
     */
    public static void addCustomerDetailsToModel(CustomerDTO customer, boolean toFetch, Model model, HttpSession session) {
        model.addAttribute(
                customer == null ? "noCustomerFound" : "customer",
                customer == null ? "Customer not found for the given details." : customer);
        addToFetchToModel(toFetch, model);
        if(!toFetch) {
            session.setAttribute("customerDetails", customer);
        }
    }

    /*
     * Adds a flag to the model indicating whether to fetch data.
     * - `toFetch`: A boolean flag to determine if data should be fetched.
     */
    public static void addToFetchToModel(boolean toFetch, Model model) {
        model.addAttribute("toFetch", toFetch);
    }

    /*
     * Adds CustomerDTO to the model and stores the initial state in the session for update comparison.
     */
    public static void addCustomerDTOForUpdateToModel(CustomerDTO customerDTO, Model model, HttpSession session) {
        model.addAttribute("customerDTO", customerDTO);
        session.setAttribute("initialCustomerDTO", customerDTO);
    }
}