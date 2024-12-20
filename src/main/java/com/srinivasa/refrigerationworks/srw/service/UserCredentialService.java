package com.srinivasa.refrigerationworks.srw.service;

import com.srinivasa.refrigerationworks.srw.entity.UserCredential;
import com.srinivasa.refrigerationworks.srw.entity.UserRole;
import com.srinivasa.refrigerationworks.srw.payload.dto.PasswordResetDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.UsernameRecoveryDTO;
import com.srinivasa.refrigerationworks.srw.repository.UserCredentialRepository;
import com.srinivasa.refrigerationworks.srw.utility.common.PhoneNumberFormatter;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.UserType;
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
     * Fetches the username associated with the provided phone number.
     * - Ensures the phone number includes the country code (+91).
     * - Delegates the query execution to the repository.
     */
    public String fetchUsername(UsernameRecoveryDTO usernameRecoveryDTO) {
        String phoneNumber = PhoneNumberFormatter.formatPhoneNumber(usernameRecoveryDTO.getPhoneNumber());
        return userCredentialRepository.fetchUsernameByPhoneNumber(phoneNumber);
    }

    /*
     * Validates the user by checking if a combination of phone number and username exists in the repository.
     * - Ensures the phone number is normalized to include the country code.
     * - Returns true if a matching user is found.
     */
    public boolean validateUser(PasswordResetDTO passwordResetDTO) {
        String phoneNumber = PhoneNumberFormatter.formatPhoneNumber(passwordResetDTO.getPhoneNumber());
        String username = passwordResetDTO.getUsername();
        return userCredentialRepository.existsByPhoneNumberAndUsername(phoneNumber, username);
    }

    /*
     * Updates the user's password in the repository.
     * - Encodes the new password before updating.
     */
    public void updatePassword(PasswordResetDTO passwordResetDTO) {
        String password = passwordEncoder.encode(passwordResetDTO.getPassword());
        String username = passwordResetDTO.getUsername();
        userCredentialRepository.updatePassword(username, password);
    }

    /*
     * Retrieves the user ID associated with the given username.
     * - Queries the database using the userCredentialRepository to find the user ID.
     */
    public String getUserIdByUsername(String username) {
        return userCredentialRepository.findUserIdByUsername(username);
    }

    /*
     * Updates the user's phone number by delegating to the repository.
     */
    public void updateUserPhoneNumber(String userId, String phoneNumber) {
        userCredentialRepository.updateUserPhoneNumber(userId, phoneNumber);
    }

    /*
     * Deactivates a user's credentials by updating the 'enabled' status to false.
     */
    public void deactivateUser(String userId) {
        userCredentialRepository.deactivateUser(userId);
    }
}