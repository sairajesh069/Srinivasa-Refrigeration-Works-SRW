package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.EmployeeModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.UserIdentifierDTO;
import com.srinivasa.refrigerationworks.srw.service.EmployeeService;
import com.srinivasa.refrigerationworks.srw.utility.common.EndpointExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;

/*
 * Controller that handles requests related to the Employee entity.
 */
@Controller
@RequestMapping("/SRW/employee")
@RequiredArgsConstructor
public class EmployeeController {

    /*
     * Service for managing employee operations.
     */
    private final EmployeeService employeeService;

    /*
     * Handles GET requests to fetch the list of all employees.
     * Adds the list of employees to the model and returns the view for displaying employee details.
     */
    @GetMapping("/list")
    public String getEmployeeList(Model model) {
        UserIdentifierDTO userIdentifierDTO = (UserIdentifierDTO) model.getAttribute("userIdentifierDTO");
        EmployeeModel.addEmployeesToModel(userIdentifierDTO == null ? new UserIdentifierDTO() : userIdentifierDTO,
                employeeService.getEmployeeList(), model);
        return "employee/employee-list";
    }

    /*
     * Handles GET requests to fetch the list of all active employees.
     * Adds the list of active employees to the model and returns the view for displaying employee details.
     */
    @GetMapping("/active-list")
    public String getActiveEmployeeList(Model model) {
        UserIdentifierDTO userIdentifierDTO = (UserIdentifierDTO) model.getAttribute("userIdentifierDTO");
        EmployeeModel.addEmployeesToModel(userIdentifierDTO == null ? new UserIdentifierDTO() : userIdentifierDTO,
                employeeService.getActiveEmployeeList(), model);
        return "employee/employee-list";
    }

    /*
     * Handles the POST request to search for an employee by their identifier.
     * Validates the input and displays the employee details if no errors occur.
     */
    @PostMapping("/search")
    public String getEmployee(@ModelAttribute @Valid UserIdentifierDTO userIdentifierDTO, BindingResult bindingResult,
                              Model model, HttpSession session, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String refererEndpoint = EndpointExtractor.employeeEndpoint(request);
        String searchEndpointOrigin = refererEndpoint.equals("search")
                ? (String) session.getAttribute("searchEndpointOrigin") : refererEndpoint;
        String identifier = userIdentifierDTO.getIdentifier();
        if(!bindingResult.hasErrors() && !identifier.isEmpty()) {
            EmployeeModel.addEmployeesToModel(userIdentifierDTO, Collections.singletonList(
                    employeeService.getEmployeeByIdentifier(identifier)), model);
            session.setAttribute("searchEndpointOrigin", searchEndpointOrigin);
            return "employee/employee-list";
        }
        redirectAttributes.addFlashAttribute("userIdentifierDTO", userIdentifierDTO);
        return "redirect:/SRW/employee/" + searchEndpointOrigin;
    }

    /*
     * Handles the GET request to fetch the logged-in employee's profile.
     */
    @GetMapping("/my-profile")
    public String getEmployeeProfile(Model model, HttpSession session) {
        EmployeeModel.addEmployeeToModel(employeeService.getEmployeeByIdentifier((String) session.getAttribute("userId")), model);
        return "employee/employee-details";
    }
}