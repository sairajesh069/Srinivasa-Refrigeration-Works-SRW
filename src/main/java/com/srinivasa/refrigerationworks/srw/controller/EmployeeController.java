package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.EmployeeModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.UserIdentifierDTO;
import com.srinivasa.refrigerationworks.srw.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * Controller that handles requests related to the Employee entity.
 * It provides a mapping to fetch and display the list of employees in the employee-list view.
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
        EmployeeModel.addEmployeeListToModel(employeeService.getEmployeeList(), model);
        return "employee/employee-list";
    }

    /*
     * Handles requests related to searching and displaying employee details.
     */
    @GetMapping("/search")
    public String getEmployee(Model model) {
        EmployeeModel.addUserIdentifierDTOToModel(model);
        return "employee/employee-details";
    }

    /*
     * Processes the employee search request.
     * Validates the user identifier and retrieves employee details.
     */
    @PostMapping("/search")
    public String getEmployee(@Valid UserIdentifierDTO userIdentifierDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "employee/employee-details";
        }
        EmployeeModel.addEmployeeDetailsToModel(
                employeeService.getEmployeeByIdentifier(userIdentifierDTO.getIdentifier()), model);
        return "employee/employee-details";
    }
}
