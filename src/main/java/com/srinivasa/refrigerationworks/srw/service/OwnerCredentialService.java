package com.srinivasa.refrigerationworks.srw.service;

import com.srinivasa.refrigerationworks.srw.entity.UserCredential;
import com.srinivasa.refrigerationworks.srw.payload.dto.OwnerCredentialDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.OwnerDTO;
import com.srinivasa.refrigerationworks.srw.utility.common.PhoneNumberFormatter;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.UserType;
import com.srinivasa.refrigerationworks.srw.utility.mapper.UserCredentialMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*
 * Service to handle OwnerCredential-related operations.
 */
@Service
@RequiredArgsConstructor
public class OwnerCredentialService {

    private final UserCredentialService userCredentialService;
    private final UserCredentialMapper userCredentialMapper;
    private final OwnerService ownerService;


    /*
     * Adds user credential for the owner. The owner's details are added,
     * and user credentials are saved with the role "ROLE_OWNER".
     */
    public void addOwnerCredential(OwnerCredentialDTO ownerCredentialDTO) {
        String ownerId = ownerService.addOwner(ownerCredentialDTO.getOwnerDTO());
        UserCredential userCredential = userCredentialMapper.toEntity(ownerCredentialDTO.getUserCredentialDTO());
        userCredential.setUserId(ownerId);
        userCredential.setPhoneNumber(ownerCredentialDTO.getOwnerDTO().getPhoneNumber());
        userCredentialService.saveCredential(userCredential, UserType.OWNER, "ROLE_OWNER");
    }

    /*
     * Updates owner details if changes are detected and synchronizes phone number.
     * - Checks for changes between initial and updated DTOs.
     * - Updates owner and phone number only if modified.
     */
    @Transactional
    public void updateOwner(OwnerDTO initialOwnerDTO, OwnerDTO updatedOwnerDTO) {
        updatedOwnerDTO.setStatus(updatedOwnerDTO.getStatus()==null ? initialOwnerDTO.getStatus() : updatedOwnerDTO.getStatus());
        if(!initialOwnerDTO.equals(updatedOwnerDTO)) {
            ownerService.updateOwner(updatedOwnerDTO);
            String initialPhoneNumber = PhoneNumberFormatter.formatPhoneNumber(initialOwnerDTO.getPhoneNumber());
            String updatedPhoneNumber = PhoneNumberFormatter.formatPhoneNumber(updatedOwnerDTO.getPhoneNumber());
            if(!updatedPhoneNumber.equals(initialPhoneNumber)) {
                userCredentialService.updateUserPhoneNumber(updatedOwnerDTO.getOwnerId(), updatedPhoneNumber);
            }
        }
    }

    /*
     * Deactivates an owner and their associated user credentials.
     * - Updates the owner's status to inactive and timestamps the deactivation.
     * - Deactivates the corresponding user credentials.
     */
    @Transactional
    public void deactivateOwner(String ownerId) {
        ownerService.deactivateOwner(ownerId);
        userCredentialService.deactivateUser(ownerId);
    }
}