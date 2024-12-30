package com.srinivasa.refrigerationworks.srw.model;

import com.srinivasa.refrigerationworks.srw.payload.dto.EmployeeDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.UserIdentifierDTO;
import org.springframework.ui.Model;

import java.util.List;

/*
 * Contains methods for adding employee-related data to the model.
 */
public class EmployeeModel {

    /*
     * Adds UserIdentifierDTO and a list of employees to the model.
     * If no employees are found, adds a "noEmployeesFound" message.
     */
    public static void addEmployeesToModel(List<EmployeeDTO> employees, Model model) {
        model.addAttribute("userIdentifierDTO", new UserIdentifierDTO());
        model.addAttribute(
                employees.isEmpty() ? "noEmployeesFound" : "employees",
                employees.isEmpty() ? "No Employee Entries in Database" : employees);
    }

    /*
     * Adds employee details to the model.
     * If employee is null, adds a "noEmployeeFound" message.
     * Otherwise, adds the employee details.
     */
    public static void addEmployeeToModel(EmployeeDTO employee, Model model) {
        model.addAttribute(
                employee == null ? "noEmployeeFound" : "employee",
                employee == null ? "Employee not found for the given details." : employee);
    }
}