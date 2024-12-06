package com.srinivasa.refrigerationworks.srw.service;

import com.srinivasa.refrigerationworks.srw.entity.UserCredential;
import com.srinivasa.refrigerationworks.srw.entity.UserRole;
import com.srinivasa.refrigerationworks.srw.payload.dto.CustomerCredentialDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.EmployeeCredentialDTO;
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
}
