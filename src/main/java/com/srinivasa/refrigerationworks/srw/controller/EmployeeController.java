package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.EmployeeModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.UserIdentifierDTO;
import com.srinivasa.refrigerationworks.srw.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * Controller that handles requests related to the Employee entity.
 */
@Controller
@RequestMapping("/SRW/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    /*
     * Handles GET requests to fetch the list of all employees.
     * Adds the list of employees to the model and returns the view for displaying employee details.
     */
    @GetMapping("/list")
    public String getEmployeeList(Model model) {
        EmployeeModel.addEmployeesToModel(employeeService.getEmployeeList(), model);
        return "employee/employee-list";
    }

    /*
     * Handles GET requests to fetch the list of all active employees.
     * Adds the list of active employees to the model and returns the view for displaying employee details.
     */
    @GetMapping("/active-list")
    public String getActiveEmployeeList(Model model) {
        EmployeeModel.addEmployeesToModel(employeeService.getActiveEmployeeList(), model);
        return "employee/employee-list";
    }

    /*
     * Handles the POST request to search for an employee by their identifier.
     * - Validates the input and displays the employee details if no errors occur.
     */
    @PostMapping("/search")
    public String getEmployee(@ModelAttribute UserIdentifierDTO userIdentifierDTO, Model model) {
        EmployeeModel.addEmployeeToModel(employeeService.getEmployeeByIdentifier(userIdentifierDTO.getIdentifier()), model);
        return "employee/employee-details";
    }
}