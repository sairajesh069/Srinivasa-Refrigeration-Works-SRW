package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.EmployeeModel;
import com.srinivasa.refrigerationworks.srw.model.UserCredentialModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.EmployeeCredentialDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.EmployeeDTO;
import com.srinivasa.refrigerationworks.srw.service.EmployeeCredentialService;
import com.srinivasa.refrigerationworks.srw.utility.UserRoleProvider;
import com.srinivasa.refrigerationworks.srw.utility.common.StringEditor;
import com.srinivasa.refrigerationworks.srw.utility.common.SubStringExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/*
 * Controller for handling employee credential operations.
 */
@Controller
@RequestMapping("/SRW/employee")
@RequiredArgsConstructor
public class EmployeeCredentialController {

    /*
     * Service for managing employee credential operations.
     */
    private final EmployeeCredentialService employeeCredentialService;

    /*
     * Initializes the binder to trim strings.
     */
    @InitBinder
    public void initialize(WebDataBinder webDataBinder) {
        StringEditor.stringTrimmer(webDataBinder);
    }

    /*
     * Displays the employee registration form.
     */
    @GetMapping("/register")
    public String createEmployee(Model model) {
        UserCredentialModel.addEmployeeCredentialToModel(model);
        return "employee/employee-register-form";
    }

    /*
     * Confirms the employee registration and adds employee credentials.
     * If validation fails, re-displays the form with error messages.
     */
    @PostMapping("/confirmation")
    public String confirmEmployee(@ModelAttribute @Valid EmployeeCredentialDTO employeeCredentialDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            UserCredentialModel.addUserFormConstantsToModel(model);
            return "employee/employee-register-form";
        }
        employeeCredentialService.addEmployeeCredential(employeeCredentialDTO);
        return "employee/employee-confirmation";
    }

    /*
     * Handles GET request to display employee update form with current employee data.
     * Sets session attribute using substring from the "Referer" header.
     * Ensures proper user role or origin before granting access.
     */
    @GetMapping("/update")
    public String updateEmployee(@RequestParam("employeeId") String employeeId, Model model,
                                 HttpSession session, HttpServletRequest request) {
        String refererEndpoint = SubStringExtractor.extractSubString(request.getHeader("Referer"), "employee/");
        if (UserRoleProvider.fetchUserRole(session).equals("ROLE_OWNER") || refererEndpoint.equals("my-profile")) {
            EmployeeModel.addEmployeeToModel(employeeCredentialService.getEmployeeById(employeeId), model);
            UserCredentialModel.addUserFormConstantsToModel(model);
            UserCredentialModel.addUpdateEndpointOriginToModel(refererEndpoint, model);
            return "employee/employee-update-form";
        }
        return "access-denied";
    }

    /*
     * Handles POST request to update employee's details after validation.
     * Redirects to origin employee page of update endpoint on success.
     */
    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute("employee") @Valid EmployeeDTO updatedEmployeeDTO, BindingResult bindingResult,
                                 @RequestParam("updateEndpointOrigin") String updateEndpointOrigin, Model model) {
        if (bindingResult.hasErrors()) {
            UserCredentialModel.addUserFormConstantsToModel(model);
            UserCredentialModel.addUpdateEndpointOriginToModel(updateEndpointOrigin, model);
            return "employee/employee-update-form";
        }
        employeeCredentialService.updateEmployee(employeeCredentialService.getEmployeeById(updatedEmployeeDTO.getEmployeeId()), updatedEmployeeDTO);
        return "redirect:/SRW/employee/" + updateEndpointOrigin;
    }

    /*
     * Handles the GET request to activate an employee.
     * Activates the employee based on the provided employeeId.
     * Redirects to the originating employee page upon success.
     */
    @GetMapping("/activate")
    public String activateEmployee(@RequestParam("employeeId") String employeeId, HttpServletRequest request) {
        employeeCredentialService.activateEmployee(employeeId);
        return "redirect:/SRW/employee/" + SubStringExtractor.extractSubString(request.getHeader("Referer"), "employee/");
    }

    /*
     * Handles the GET request to deactivate an employee.
     * Deactivates the employee based on the provided employeeId.
     * Redirects to the originating employee page upon success.
     */
    @GetMapping("/deactivate")
    public String deactivateEmployee(@RequestParam("employeeId") String employeeId, HttpServletRequest request) {
        employeeCredentialService.deactivateEmployee(employeeId);
        return "redirect:/SRW/employee/" + SubStringExtractor.extractSubString(request.getHeader("Referer"), "employee/");
    }
}