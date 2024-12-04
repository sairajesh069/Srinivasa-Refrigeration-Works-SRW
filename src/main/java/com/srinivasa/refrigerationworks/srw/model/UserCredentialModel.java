package com.srinivasa.refrigerationworks.srw.model;

import com.srinivasa.refrigerationworks.srw.payload.dto.OwnerCredentialDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.OwnerDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.UserCredentialDTO;
import com.srinivasa.refrigerationworks.srw.utility.common.constants.UserFormConstants;
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
        addOwnerFormConstantsToModel(model);
    }

    /*
     * Adds form constants (like genders) to the model for owner registration.
     */
    public static void addOwnerFormConstantsToModel(Model model) {
        model.addAttribute("genders", UserFormConstants.GENDERS);
    }
}
