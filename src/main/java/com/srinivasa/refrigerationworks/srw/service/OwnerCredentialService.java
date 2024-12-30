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

    /*
     * UserCredentialService for handling user credentials related operations.
     */
    private final UserCredentialService userCredentialService;

    /*
     * UserCredentialMapper for mapping OwnerCredentialDTO to UserCredential entity and vice versa.
     */
    private final UserCredentialMapper userCredentialMapper;

    /*
     * OwnerService for handling owner-related operations.
     */
    private final OwnerService ownerService;

    /*
     * Adds user credential for the owner and saves them with the role "ROLE_OWNER".
     */
    public void addOwnerCredential(OwnerCredentialDTO ownerCredentialDTO) {
        String ownerId = ownerService.addOwner(ownerCredentialDTO.getOwnerDTO());
        UserCredential userCredential = userCredentialMapper.toEntity(ownerCredentialDTO.getUserCredentialDTO());
        userCredential.setUserId(ownerId);
        userCredential.setPhoneNumber(ownerCredentialDTO.getOwnerDTO().getPhoneNumber());
        userCredentialService.saveCredential(userCredential, UserType.OWNER, "ROLE_OWNER");
    }

    /*
     * Updates owner details if changes are detected and synchronizes phone number if modified.
     */
    @Transactional
    public void updateOwner(OwnerDTO initialOwnerDTO, OwnerDTO updatedOwnerDTO) {
        updatedOwnerDTO.setStatus(updatedOwnerDTO.getStatus() == null ? initialOwnerDTO.getStatus() : updatedOwnerDTO.getStatus());
        if (!initialOwnerDTO.equals(updatedOwnerDTO)) {
            ownerService.updateOwner(updatedOwnerDTO);
            String initialPhoneNumber = PhoneNumberFormatter.formatPhoneNumber(initialOwnerDTO.getPhoneNumber());
            String updatedPhoneNumber = PhoneNumberFormatter.formatPhoneNumber(updatedOwnerDTO.getPhoneNumber());
            if (!updatedPhoneNumber.equals(initialPhoneNumber)) {
                userCredentialService.updateUserPhoneNumber(updatedOwnerDTO.getOwnerId(), updatedPhoneNumber);
            }
        }
    }

    /*
     * Activates an owner and their associated user credentials.
     */
    @Transactional
    public void activateOwner(String ownerId) {
        ownerService.activateOwner(ownerId);
        userCredentialService.updateUserStatus(ownerId, (byte) 1);
    }

    /*
     * Deactivates an owner and their associated user credentials.
     */
    @Transactional
    public void deactivateOwner(String ownerId) {
        ownerService.deactivateOwner(ownerId);
        userCredentialService.updateUserStatus(ownerId, (byte) 0);
    }

    /*
     * Retrieves the OwnerDTO by ownerId.
     */
    public OwnerDTO getOwnerById(String ownerId) {
        return ownerService.getOwnerByIdentifier(ownerId);
    }
}