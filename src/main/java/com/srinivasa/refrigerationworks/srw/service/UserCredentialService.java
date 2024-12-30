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

    /*
     * UserCredentialRepository for handling user credential-related data operations.
     */
    private final UserCredentialRepository userCredentialRepository;

    /*
     * PasswordEncoder for encoding user passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /*
     * Saves user credentials, encodes the password, and associates user roles.
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
     * Formats the phone number to include the country code (+91).
     */
    @Cacheable(value = "user-credential", key = "'fetch_user-' + #usernameRecoveryDTO.phoneNumber")
    public String fetchUsername(UsernameRecoveryDTO usernameRecoveryDTO) {
        return userCredentialRepository.fetchUsernameByPhoneNumber(
                PhoneNumberFormatter.formatPhoneNumber(usernameRecoveryDTO.getPhoneNumber()));
    }

    /*
     * Validates user by checking if the combination of phone number and username exists.
     * Formats the phone number to include the country code (+91).
     */
    @Cacheable(value = "user-credential", key = "'validate-' + #passwordResetDTO.username + '&' + #passwordResetDTO.phoneNumber")
    public boolean validateUser(PasswordResetDTO passwordResetDTO) {
        return userCredentialRepository.existsByPhoneNumberAndUsername(
                PhoneNumberFormatter.formatPhoneNumber(passwordResetDTO.getPhoneNumber()), passwordResetDTO.getUsername());
    }

    /*
     * Updates the user's password by encoding the new password before updating.
     */
    public void updatePassword(PasswordResetDTO passwordResetDTO) {
        userCredentialRepository.updatePassword(passwordResetDTO.getUsername(), passwordEncoder.encode(passwordResetDTO.getPassword()));
    }

    /*
     * Retrieves the user ID associated with the given username.
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
     * Activates or deactivates a user's credentials by updating the 'enabled' status.
     */
    public void updateUserStatus(String userId, byte enabled) {
        userCredentialRepository.updateUserStatus(userId, enabled);
    }
}