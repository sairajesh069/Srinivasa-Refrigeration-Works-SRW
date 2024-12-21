package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.UserCredentialModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.EmployeeCredentialDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.EmployeeDTO;
import com.srinivasa.refrigerationworks.srw.service.EmployeeCredentialService;
import com.srinivasa.refrigerationworks.srw.utility.common.StringEditor;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/*
 * Controller for handling employee credential operations
 */
@Controller
@RequestMapping("/SRW/employee")
@RequiredArgsConstructor
public class EmployeeCredentialController {

    private final EmployeeCredentialService employeeCredentialService;

    /*
     * Initializes the binder to trim strings
     */
    @InitBinder
    public void initialize(WebDataBinder webDataBinder) {
        StringEditor.stringTrimmer(webDataBinder);
    }

    /*
     * Confirms the employee registration and adds employee credentials
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
     * Handles POST request to update employee's details after validation.
     * Redirects to employee list on success.
     */
    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute("employeeDTO") @Valid EmployeeDTO updatedEmployeeDTO, BindingResult bindingResult, Model model, HttpSession session) {
        if(bindingResult.hasErrors()) {
            UserCredentialModel.addUserFormConstantsToModel(model);
            return "employee/employee-update-form";
        }
        employeeCredentialService.updateEmployee((EmployeeDTO) session.getAttribute("initialEmployeeDTO"), updatedEmployeeDTO);
        return "redirect:/SRW/employee/list";
    }

    /*
     * Handles the GET request to activate an employee.
     * - Activates the employee based on the provided employeeId.
     * - Redirects to the employee list page upon success.
     */
    @GetMapping("/activate")
    public String activateEmployee(@RequestParam("employeeId") String employeeId) {
        employeeCredentialService.activateEmployee(employeeId);
        return "redirect:/SRW/employee/list";
    }

    /*
     * Handles the GET request to deactivate an employee.
     * - Deactivates the employee based on the provided employeeId.
     * - Redirects to the employee list page upon success.
     */
    @GetMapping("/deactivate")
    public String deactivateEmployee(@RequestParam("employeeId") String employeeId) {
        employeeCredentialService.deactivateEmployee(employeeId);
        return "redirect:/SRW/employee/list";
    }
}