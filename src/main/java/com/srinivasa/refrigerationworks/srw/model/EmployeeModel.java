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
     * Adds a new UserIdentifierDTO object to the model for user input.
     * Also adds a flag to indicate that data should be fetched.
     */
    public static void addUserIdentifierDTOToModel(Model model) {
        model.addAttribute("userIdentifierDTO", new UserIdentifierDTO());
        addToFetchToModel(true, model);
    }

    /*
     * Adds employee details to the model.
     * - If employee is null, adds a message indicating no employee found.
     * - Otherwise, adds the employee details.
     * - Also adds a flag to indicate whether data should be fetched.
     */
    public static void addEmployeeDetailsToModel(EmployeeDTO employee, boolean toFetch, Model model, HttpSession session) {
        model.addAttribute(
                employee == null ? "noEmployeeFound" : "employee",
                employee == null ? "Employee not found for the given details." : employee);
        addToFetchToModel(toFetch, model);
        if(!toFetch) {
            session.setAttribute("employeeDetails", employee);
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
     * Adds EmployeeDTO to the model and stores the initial state in the session for update comparison.
     */
    public static void addEmployeeDTOForUpdateToModel(EmployeeDTO employeeDTO, Model model, HttpSession session) {
        model.addAttribute("employeeDTO", employeeDTO);
        session.setAttribute("initialEmployeeDTO", employeeDTO);
    }
}