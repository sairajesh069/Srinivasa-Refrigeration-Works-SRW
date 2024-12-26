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
     * Adds UserIdentifierDTO to the model.
     * Adds a list of employees to the model, or a message if no employees are found.
     * The employees list is added to the model under the attribute "employees" if not empty,
     * otherwise a "noEmployeesFound" message is added.
     */
    public static void addEmployeeListToModel(List<EmployeeDTO> employees, Model model) {
        model.addAttribute("userIdentifierDTO", new UserIdentifierDTO());
        model.addAttribute(
                employees.isEmpty() ? "noEmployeesFound" : "employees",
                employees.isEmpty() ? "No Employee Entries in Database" : employees);
    }

    /*
     * Adds employee details to the model.
     * - If employee is null, adds a message indicating no employee found.
     * - Otherwise, adds the employee details.
     */
    public static void addEmployeeDetailsToModel(EmployeeDTO employee, Model model, HttpSession session) {
        model.addAttribute(
                employee == null ? "noEmployeeFound" : "employee",
                employee == null ? "Employee not found for the given details." : employee);
        session.setAttribute("employeeDetails", employee);
    }

    /*
     * Adds EmployeeDTO to the model and stores the initial state in the session for update comparison.
     */
    public static void addEmployeeDTOForUpdateToModel(EmployeeDTO employeeDTO, Model model, HttpSession session) {
        model.addAttribute("employeeDTO", employeeDTO);
        session.setAttribute("initialEmployeeDTO", employeeDTO);
    }
}