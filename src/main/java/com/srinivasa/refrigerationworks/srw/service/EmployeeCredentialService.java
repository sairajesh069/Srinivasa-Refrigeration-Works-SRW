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

    /*
     * UserCredentialService for managing user credentials.
     */
    private final UserCredentialService userCredentialService;

    /*
     * UserCredentialMapper to map UserCredentialDTO to UserCredential entity.
     */
    private final UserCredentialMapper userCredentialMapper;

    /*
     * EmployeeService for handling employee-related operations.
     */
    private final EmployeeService employeeService;

    /*
     * Adds user credential for the employee and saves it with the role "ROLE_EMPLOYEE".
     */
    public void addEmployeeCredential(EmployeeCredentialDTO employeeCredentialDTO) {
        String employeeId = employeeService.addEmployee(employeeCredentialDTO.getEmployeeDTO());
        UserCredential userCredential = userCredentialMapper.toEntity(employeeCredentialDTO.getUserCredentialDTO());
        userCredential.setUserId(employeeId);
        userCredential.setPhoneNumber(employeeCredentialDTO.getEmployeeDTO().getPhoneNumber());
        userCredentialService.saveCredential(userCredential, UserType.EMPLOYEE, "ROLE_EMPLOYEE");
    }

    /*
     * Updates employee details and synchronizes the phone number if changed.
     */
    @Transactional
    public void updateEmployee(EmployeeDTO initialEmployeeDTO, EmployeeDTO updatedEmployeeDTO) {
        updatedEmployeeDTO.setStatus(updatedEmployeeDTO.getStatus() == null ? initialEmployeeDTO.getStatus() : updatedEmployeeDTO.getStatus());
        updatedEmployeeDTO.setDateOfHire(initialEmployeeDTO.getDateOfHire());
        if (!initialEmployeeDTO.equals(updatedEmployeeDTO)) {
            employeeService.updateEmployee(updatedEmployeeDTO);
            String initialPhoneNumber = PhoneNumberFormatter.formatPhoneNumber(initialEmployeeDTO.getPhoneNumber());
            String updatedPhoneNumber = PhoneNumberFormatter.formatPhoneNumber(updatedEmployeeDTO.getPhoneNumber());
            if (!updatedPhoneNumber.equals(initialPhoneNumber)) {
                userCredentialService.updateUserPhoneNumber(updatedEmployeeDTO.getEmployeeId(), updatedPhoneNumber);
            }
        }
    }

    /*
     * Activates an employee and their user credentials.
     */
    @Transactional
    public void activateEmployee(String employeeId) {
        employeeService.activateEmployee(employeeId);
        userCredentialService.updateUserStatus(employeeId, (byte) 1);
    }

    /*
     * Deactivates an employee and their user credentials.
     */
    @Transactional
    public void deactivateEmployee(String employeeId) {
        employeeService.deactivateEmployee(employeeId);
        userCredentialService.updateUserStatus(employeeId, (byte) 0);
    }

    /*
     * Retrieves EmployeeDTO by employeeId.
     */
    public EmployeeDTO getEmployeeById(String employeeId) {
        return employeeService.getEmployeeByIdentifier(employeeId);
    }
}