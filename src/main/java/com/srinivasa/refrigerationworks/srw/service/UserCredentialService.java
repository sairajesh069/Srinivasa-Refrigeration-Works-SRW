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
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "user-credential", key = "'fetch_user-' + #usernameRecoveryDTO.phoneNumber")
    public String fetchUsername(UsernameRecoveryDTO usernameRecoveryDTO) {
        return userCredentialRepository.fetchUsernameByPhoneNumber(
                PhoneNumberFormatter.formatPhoneNumber(usernameRecoveryDTO.getPhoneNumber()));
    }

    /*
     * Validates the user by checking if a combination of phone number and username exists in the repository.
     * - Ensures the phone number is normalized to include the country code.
     * - Returns true if a matching user is found.
     */
    @Cacheable(value = "user-credential", key = "'validate-' + #passwordResetDTO.username + '&' + #passwordResetDTO.phoneNumber")
    public boolean validateUser(PasswordResetDTO passwordResetDTO) {
        return userCredentialRepository.existsByPhoneNumberAndUsername(
                PhoneNumberFormatter.formatPhoneNumber(passwordResetDTO.getPhoneNumber()), passwordResetDTO.getUsername());
    }

    /*
     * Updates the user's password in the repository.
     * - Encodes the new password before updating.
     */
    public void updatePassword(PasswordResetDTO passwordResetDTO) {
        userCredentialRepository.updatePassword(passwordResetDTO.getUsername(), passwordEncoder.encode(passwordResetDTO.getPassword()));
    }

    /*
     * Retrieves the user ID associated with the given username.
     * - Queries the database using the userCredentialRepository to find the user ID.
     */
    @Cacheable(value = "user-credential", key = "'user-' + #username")
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
     * Activates/Deactivates a user's credentials by updating the 'enabled' status to true or false.
     */
    public void updateUserStatus(String userId, byte enabled) {
        userCredentialRepository.updateUserStatus(userId, enabled);
    }
}