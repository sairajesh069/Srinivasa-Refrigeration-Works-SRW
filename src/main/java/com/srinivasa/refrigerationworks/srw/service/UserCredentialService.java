package com.srinivasa.refrigerationworks.srw.service;

import com.srinivasa.refrigerationworks.srw.entity.UserCredential;
import com.srinivasa.refrigerationworks.srw.entity.UserRole;
import com.srinivasa.refrigerationworks.srw.payload.dto.OwnerCredentialDTO;
import com.srinivasa.refrigerationworks.srw.repository.UserCredentialRepository;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.UserType;
import com.srinivasa.refrigerationworks.srw.utility.mapper.UserCredentialMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/*
 * Service to handle UserCredential-related operations.
 */
@Service
@RequiredArgsConstructor
public class UserCredentialService {

    private final UserCredentialRepository userCredentialRepository;
    private final UserCredentialMapper userCredentialMapper;
    private final OwnerService ownerService;
    private final PasswordEncoder passwordEncoder;

    /*
     * Saves user credentials with the specified user type and role.
     * The password is encoded before saving, and a UserRole is created
     * and associated with the user.
     */
    @Transactional
    public void saveCredential(UserCredential userCredential, UserType userType, String role) {
        userCredential.setUserType(userType);
        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        UserRole userRole = new UserRole(userCredential.getUsername(), role);
        userRole.setUserCredential(userCredential);
        userCredential.addUserRole(userRole);
        userCredentialRepository.save(userCredential);
    }

    /*
     * Adds user credential for the owner. The owner's details are added,
     * and user credentials are saved with the role "ROLE_OWNER".
     */
    public void addOwnerCredential(OwnerCredentialDTO ownerCredentialDTO) {
        String ownerId = ownerService.addOwner(ownerCredentialDTO.getOwnerDTO());
        UserCredential userCredential = userCredentialMapper.toEntity(ownerCredentialDTO.getUserCredentialDTO());
        userCredential.setUserId(ownerId);
        userCredential.setPhoneNumber(ownerCredentialDTO.getOwnerDTO().getPhoneNumber());
        saveCredential(userCredential, UserType.OWNER, "ROLE_OWNER");
    }
}
