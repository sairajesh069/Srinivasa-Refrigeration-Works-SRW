package com.srinivasa.refrigerationworks.srw.model;

import com.srinivasa.refrigerationworks.srw.payload.dto.EmployeeDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.UserIdentifierDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.List;

/*
 * Contains methods for adding employee-related data to the model.
 */
public class EmployeeModel {

    /*
     * Adds a list of employees to the model, or a message if no employees are found.
     * The employees list is added to the model under the attribute "employees" if not empty,
     * otherwise a "noEmployeesFound" message is added.
     */
    public static void addEmployeeListToModel(List<EmployeeDTO> employees, Model model) {
        model.addAttribute(
                employees.isEmpty() ? "noEmployeesFound" : "employees",
                employees.isEmpty() ? "No Employee Entries in Database" : employees);
    }

    /*
     * Adds a new UserIdentifierDTO object to the model.
     * Used to capture user input for searching an employee.
     */
    public static void addUserIdentifierDTOToModel(Model model) {
        model.addAttribute("userIdentifierDTO", new UserIdentifierDTO());
    }

    /*
     * Adds employee details or a fallback message to the model.
     * If the employee is found, adds the EmployeeDTO object; otherwise, adds an error message.
     */
    public static void addEmployeeDetailsToModel(EmployeeDTO employee, Model model) {
        model.addAttribute(
                employee == null ? "noEmployeeFound" : "employee",
                employee == null ? "Employee not found for the given details." : employee);
    }

    /*
     * Adds EmployeeDTO to the model and stores the initial state in the session for update comparison.
     */
    public static void addEmployeeDTOForUpdateToModel(EmployeeDTO employeeDTO, Model model, HttpSession session) {
        model.addAttribute("employeeDTO", employeeDTO);
        session.setAttribute("initialEmployeeDTO", employeeDTO);
    }
}