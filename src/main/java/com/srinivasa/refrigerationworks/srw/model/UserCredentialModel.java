package com.srinivasa.refrigerationworks.srw.model;

import com.srinivasa.refrigerationworks.srw.payload.dto.*;
import com.srinivasa.refrigerationworks.srw.utility.common.SubStringExtractor;
import com.srinivasa.refrigerationworks.srw.utility.common.constants.UserFormConstants;
import org.springframework.ui.Model;

/*
 * Model class for handling user and credential related form data.
 */
public class UserCredentialModel {

    /*
     * Adds OwnerCredentialDTO and form constants for owner registration.
     */
    public static void addOwnerCredentialToModel(Model model) {
        OwnerCredentialDTO ownerCredentialDTO = new OwnerCredentialDTO();
        ownerCredentialDTO.setOwnerDTO(new OwnerDTO());
        ownerCredentialDTO.setUserCredentialDTO(new UserCredentialDTO());
        model.addAttribute("ownerCredentialDTO", ownerCredentialDTO);
        addUserFormConstantsToModel(model);
    }

    /*
     * Adds EmployeeCredentialDTO and form constants for employee registration.
     */
    public static void addEmployeeCredentialToModel(Model model) {
        EmployeeCredentialDTO employeeCredentialDTO = new EmployeeCredentialDTO();
        employeeCredentialDTO.setEmployeeDTO(new EmployeeDTO());
        employeeCredentialDTO.setUserCredentialDTO(new UserCredentialDTO());
        model.addAttribute("employeeCredentialDTO", employeeCredentialDTO);
        addUserFormConstantsToModel(model);
    }

    /*
     * Adds CustomerCredentialDTO and form constants for customer registration.
     */
    public static void addCustomerCredentialToModel(Model model) {
        CustomerCredentialDTO customerCredentialDTO = new CustomerCredentialDTO();
        customerCredentialDTO.setCustomerDTO(new CustomerDTO());
        customerCredentialDTO.setUserCredentialDTO(new UserCredentialDTO());
        model.addAttribute("customerCredentialDTO", customerCredentialDTO);
        addUserFormConstantsToModel(model);
    }

    /*
     * Adds form constants (genders) for user registration.
     */
    public static void addUserFormConstantsToModel(Model model) {
        model.addAttribute("genders", UserFormConstants.GENDERS);
    }

    /*
     * Adds user profile URL based on user type.
     */
    public static void addUserProfileHrefToModel(String userType, Model model) {
        model.addAttribute("profileHref", "/SRW/" + SubStringExtractor.extractSubString(userType, "ROLE_").toLowerCase() + "/my-profile");
    }

    /*
     * Adds update endpoint origin to the model.
     */
    public static void addUpdateEndpointOriginToModel(String updateEndpointOrigin, Model model) {
        model.addAttribute("updateEndpointOrigin", updateEndpointOrigin);
    }
}