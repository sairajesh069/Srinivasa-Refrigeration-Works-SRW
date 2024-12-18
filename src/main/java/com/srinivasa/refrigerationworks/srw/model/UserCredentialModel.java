package com.srinivasa.refrigerationworks.srw.model;

import com.srinivasa.refrigerationworks.srw.payload.dto.*;
import com.srinivasa.refrigerationworks.srw.utility.common.constants.UserFormConstants;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

/*
 * Model class for handling user registration form data.
 */
public class UserCredentialModel {

    /*
     * Initializes the OwnerDTO and UserCredentialDTO.
     * Adds OwnerCredentialDTO and form constants to the model for owner registration.
     */
    public static void addOwnerCredentialToModel(Model model) {
        OwnerCredentialDTO ownerCredentialDTO = new OwnerCredentialDTO();
        ownerCredentialDTO.setOwnerDTO(new OwnerDTO());
        ownerCredentialDTO.setUserCredentialDTO(new UserCredentialDTO());
        model.addAttribute("ownerCredentialDTO", ownerCredentialDTO);
        addUserFormConstantsToModel(model);
    }

    /*
     * Initializes the EmployeeDTO and UserCredentialDTO.
     * Adds EmployeeCredentialDTO and form constants to the model for employee registration.
     */
    public static void addEmployeeCredentialToModel(Model model) {
        EmployeeCredentialDTO employeeCredentialDTO = new EmployeeCredentialDTO();
        employeeCredentialDTO.setEmployeeDTO(new EmployeeDTO());
        employeeCredentialDTO.setUserCredentialDTO(new UserCredentialDTO());
        model.addAttribute("employeeCredentialDTO", employeeCredentialDTO);
        addUserFormConstantsToModel(model);
    }

    /*
     * Initializes the CustomerDTO and UserCredentialDTO.
     * Adds CustomerCredentialDTO and form constants to the model for customer registration.
     */
    public static void addCustomerCredentialToModel(Model model) {
        CustomerCredentialDTO customerCredentialDTO = new CustomerCredentialDTO();
        customerCredentialDTO.setCustomerDTO(new CustomerDTO());
        customerCredentialDTO.setUserCredentialDTO(new UserCredentialDTO());
        model.addAttribute("customerCredentialDTO", customerCredentialDTO);
        addUserFormConstantsToModel(model);
    }

    /*
     * Adds form constants (like genders) to the model for user registration.
     */
    public static void addUserFormConstantsToModel(Model model) {
        model.addAttribute("genders", UserFormConstants.GENDERS);
    }

    /*
     * Adds OwnerDTO to the model and stores the initial state in the session for update comparison.
     */
    public static void addOwnerDTOForUpdateToModel(OwnerDTO ownerDTO, Model model, HttpSession session) {
        model.addAttribute("ownerDTO", ownerDTO);
        session.setAttribute("initialOwnerDTO", ownerDTO);
    }

    /*
     * Adds EmployeeDTO to the model and stores the initial state in the session for update comparison.
     */
    public static void addEmployeeDTOForUpdateToModel(EmployeeDTO employeeDTO, Model model, HttpSession session) {
        model.addAttribute("employeeDTO", employeeDTO);
        session.setAttribute("initialEmployeeDTO", employeeDTO);
    }
}
