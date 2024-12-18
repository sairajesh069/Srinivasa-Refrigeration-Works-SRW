package com.srinivasa.refrigerationworks.srw.service;

import com.srinivasa.refrigerationworks.srw.entity.UserCredential;
import com.srinivasa.refrigerationworks.srw.entity.UserRole;
import com.srinivasa.refrigerationworks.srw.payload.dto.*;
import com.srinivasa.refrigerationworks.srw.repository.UserCredentialRepository;
import com.srinivasa.refrigerationworks.srw.utility.common.PhoneNumberFormatter;
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
    private final PasswordEncoder passwordEncoder;
    private final OwnerService ownerService;
    private final EmployeeService employeeService;
    private final CustomerService customerService;

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

    /*
     * Adds user credential for the employee. The employee's details are added,
     * and user credentials are saved with the role "ROLE_EMPLOYEE".
     */
    public void addEmployeeCredential(EmployeeCredentialDTO employeeCredentialDTO) {
        String employeeId = employeeService.addEmployee(employeeCredentialDTO.getEmployeeDTO());
        UserCredential userCredential = userCredentialMapper.toEntity(employeeCredentialDTO.getUserCredentialDTO());
        userCredential.setUserId(employeeId);
        userCredential.setPhoneNumber(employeeCredentialDTO.getEmployeeDTO().getPhoneNumber());
        saveCredential(userCredential, UserType.EMPLOYEE, "ROLE_EMPLOYEE");
    }

    /*
     * Adds user credential for the customer. The customer's details are added,
     * and user credentials are saved with the role "ROLE_CUSTOMER".
     */
    public void addCustomerCredential(CustomerCredentialDTO customerCredentialDTO) {
        String customerId = customerService.addCustomer(customerCredentialDTO.getCustomerDTO());
        UserCredential userCredential = userCredentialMapper.toEntity(customerCredentialDTO.getUserCredentialDTO());
        userCredential.setUserId(customerId);
        userCredential.setPhoneNumber(customerCredentialDTO.getCustomerDTO().getPhoneNumber());
        saveCredential(userCredential, UserType.CUSTOMER, "ROLE_CUSTOMER");
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
     * Fetches the user type (role) associated with the provided username.
     */
    public String getUserTypeByUsername(String username) {
        return userCredentialRepository.findUserTypeByUsername(username);
    }

    /*
     * Updates the user's phone number by delegating to the repository.
     */
    public void updateUserPhoneNumber(String userId, String phoneNumber) {
        userCredentialRepository.updateUserPhoneNumber(userId, phoneNumber);
    }

    /*
     * Updates owner details if changes are detected and synchronizes phone number.
     * - Checks for changes between initial and updated DTOs.
     * - Updates owner and phone number only if modified.
     */
    @Transactional
    public void updateOwner(OwnerDTO initialOwnerDTO, OwnerDTO updatedOwnerDTO) {
        if(!initialOwnerDTO.equals(updatedOwnerDTO)) {
            ownerService.updateOwner(updatedOwnerDTO);
            String initialPhoneNumber = PhoneNumberFormatter.formatPhoneNumber(initialOwnerDTO.getPhoneNumber());
            String updatedPhoneNumber = PhoneNumberFormatter.formatPhoneNumber(updatedOwnerDTO.getPhoneNumber());
            if(!updatedPhoneNumber.equals(initialPhoneNumber)) {
                updateUserPhoneNumber(updatedOwnerDTO.getOwnerId(), updatedPhoneNumber);
            }
        }
    }

    /*
     * Updates employee details if changes are detected and synchronizes phone number.
     * - Checks for changes between initial and updated DTOs.
     * - Updates employee and phone number only if modified.
     */
    @Transactional
    public void updateEmployee(EmployeeDTO initialEmployeeDTO, EmployeeDTO updatedEmployeeDTO) {
        updatedEmployeeDTO.setDateOfHire(initialEmployeeDTO.getDateOfHire());
        if(!initialEmployeeDTO.equals(updatedEmployeeDTO)) {
            employeeService.updateEmployee(updatedEmployeeDTO);
            String initialPhoneNumber = PhoneNumberFormatter.formatPhoneNumber(initialEmployeeDTO.getPhoneNumber());
            String updatedPhoneNumber = PhoneNumberFormatter.formatPhoneNumber(updatedEmployeeDTO.getPhoneNumber());
            if(!updatedPhoneNumber.equals(initialPhoneNumber)) {
                updateUserPhoneNumber(updatedEmployeeDTO.getEmployeeId(), updatedPhoneNumber);
            }
        }
    }

    /*
     * Updates customer details if changes are detected and synchronizes phone number.
     * - Checks for changes between initial and updated DTOs.
     * - Updates customer and phone number only if modified.
     */
    @Transactional
    public void updateCustomer(CustomerDTO initialCustomerDTO, CustomerDTO updatedCustomerDTO) {
        if(!initialCustomerDTO.equals(updatedCustomerDTO)) {
            customerService.updateCustomer(updatedCustomerDTO);
            String initialPhoneNumber = PhoneNumberFormatter.formatPhoneNumber(initialCustomerDTO.getPhoneNumber());
            String updatedPhoneNumber = PhoneNumberFormatter.formatPhoneNumber(updatedCustomerDTO.getPhoneNumber());
            if(!updatedPhoneNumber.equals(initialPhoneNumber)) {
                updateUserPhoneNumber(updatedCustomerDTO.getCustomerId(), updatedPhoneNumber);
            }
        }
    }
}