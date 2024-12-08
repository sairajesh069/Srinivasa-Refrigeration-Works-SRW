package com.srinivasa.refrigerationworks.srw.model;

import com.srinivasa.refrigerationworks.srw.payload.dto.EmployeeDTO;
import org.springframework.ui.Model;

import java.util.List;

/*
 * Contains methods for adding employee-related data to the model.
 * It provides functionality for adding a list of employees to the model.
 */
public class EmployeeModel {

    /*
     * Adds a list of employees to the model, or a message if no employees are found.
     * The employees list is added to the model under the attribute "employees" if not empty,
     * otherwise a "noUsersFound" message is added.
     */
    public static void addEmployeeListToModel(List<EmployeeDTO> employees, Model model) {
        model.addAttribute(
                employees.isEmpty() ? "noUsersFound" : "employees",
                employees.isEmpty() ? "No Employee Entries in Database" : employees);
    }
}
