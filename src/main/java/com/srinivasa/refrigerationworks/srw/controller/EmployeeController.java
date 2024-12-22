package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.EmployeeModel;
import com.srinivasa.refrigerationworks.srw.model.UserCredentialModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.UserIdentifierDTO;
import com.srinivasa.refrigerationworks.srw.service.EmployeeService;
import com.srinivasa.refrigerationworks.srw.utility.common.SubStringExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*
 * Controller that handles requests related to the Employee entity.
 */
@Controller
@RequestMapping("/SRW/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    /*
     * Displays the employee registration form
     */
    @GetMapping("/register")
    public String createEmployee(Model model) {
        UserCredentialModel.addEmployeeCredentialToModel(model);
        return "employee/employee-register-form";
    }

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
     * Handles GET requests to fetch the list of all active employees.
     * Adds the list of active employees to the model and returns the view for displaying employee details.
     */
    @GetMapping("/active-list")
    public String getActiveEmployeeList(Model model) {
        EmployeeModel.addEmployeeListToModel(employeeService.getActiveEmployeeList(), model);
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

    /*
     * Handles GET request to display employee update form with current employee data.
     * Sets session attribute using substring from the "Referer" header.
     */
    @GetMapping("/update")
    public String updateEmployee(@RequestParam("employeeId") String employeeId, Model model, HttpSession session, HttpServletRequest request) {
        EmployeeModel.addEmployeeDTOForUpdateToModel(employeeService.getEmployeeByIdentifier(employeeId), model, session);
        UserCredentialModel.addUserFormConstantsToModel(model);
        session.setAttribute("updateEndpointOrigin", SubStringExtractor.extractSubString(request.getHeader("Referer"), "employee/"));
        return "employee/employee-update-form";
    }
}