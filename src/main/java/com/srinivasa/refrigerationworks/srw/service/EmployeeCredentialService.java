package com.srinivasa.refrigerationworks.srw.service;

import com.srinivasa.refrigerationworks.srw.entity.UserCredential;
import com.srinivasa.refrigerationworks.srw.payload.dto.EmployeeCredentialDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.EmployeeDTO;
import com.srinivasa.refrigerationworks.srw.utility.common.PhoneNumberFormatter;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.UserType;
import com.srinivasa.refrigerationworks.srw.utility.mapper.UserCredentialMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*
 * Service to handle EmployeeCredential-related operations.
 */
@Service
@RequiredArgsConstructor
public class EmployeeCredentialService {

    private final UserCredentialService userCredentialService;
    private final UserCredentialMapper userCredentialMapper;
    private final EmployeeService employeeService;

    /*
     * Adds user credential for the employee. The employee's details are added,
     * and user credentials are saved with the role "ROLE_EMPLOYEE".
     */
    public void addEmployeeCredential(EmployeeCredentialDTO employeeCredentialDTO) {
        String employeeId = employeeService.addEmployee(employeeCredentialDTO.getEmployeeDTO());
        UserCredential userCredential = userCredentialMapper.toEntity(employeeCredentialDTO.getUserCredentialDTO());
        userCredential.setUserId(employeeId);
        userCredential.setPhoneNumber(employeeCredentialDTO.getEmployeeDTO().getPhoneNumber());
        userCredentialService.saveCredential(userCredential, UserType.EMPLOYEE, "ROLE_EMPLOYEE");
    }

    /*
     * Updates employee details if changes are detected and synchronizes phone number.
     * - Checks for changes between initial and updated DTOs.
     * - Updates employee and phone number only if modified.
     */
    @Transactional
    public void updateEmployee(EmployeeDTO initialEmployeeDTO, EmployeeDTO updatedEmployeeDTO) {
        updatedEmployeeDTO.setStatus(updatedEmployeeDTO.getStatus()==null ? initialEmployeeDTO.getStatus() : updatedEmployeeDTO.getStatus());
        updatedEmployeeDTO.setDateOfHire(initialEmployeeDTO.getDateOfHire());
        if(!initialEmployeeDTO.equals(updatedEmployeeDTO)) {
            employeeService.updateEmployee(updatedEmployeeDTO);
            String initialPhoneNumber = PhoneNumberFormatter.formatPhoneNumber(initialEmployeeDTO.getPhoneNumber());
            String updatedPhoneNumber = PhoneNumberFormatter.formatPhoneNumber(updatedEmployeeDTO.getPhoneNumber());
            if(!updatedPhoneNumber.equals(initialPhoneNumber)) {
                userCredentialService.updateUserPhoneNumber(updatedEmployeeDTO.getEmployeeId(), updatedPhoneNumber);
            }
        }
    }

    /*
     * Activates an employee and their associated user credentials.
     * - Updates the employee's status to active and timestamps the activation.
     * - Activates the corresponding user credentials.
     */
    @Transactional
    public void activateEmployee(String employeeId) {
        employeeService.activateEmployee(employeeId);
        userCredentialService.updateUserStatus(employeeId, (byte) 1);
    }

    /*
     * Deactivates an employee and their associated user credentials.
     * - Updates the employee's status to inactive and timestamps the deactivation.
     * - Deactivates the corresponding user credentials.
     */
    @Transactional
    public void deactivateEmployee(String employeeId) {
        employeeService.deactivateEmployee(employeeId);
        userCredentialService.updateUserStatus(employeeId, (byte) 0);
    }

    /*
     * Retrieves the EmployeeDTO by employeeId.
     */
    public EmployeeDTO getEmployeeById(String employeeId) {
        return employeeService.getEmployeeByIdentifier(employeeId);
    }
}