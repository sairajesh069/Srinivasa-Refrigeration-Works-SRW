package com.srinivasa.refrigerationworks.srw.service;

import com.srinivasa.refrigerationworks.srw.entity.Employee;
import com.srinivasa.refrigerationworks.srw.payload.dto.EmployeeDTO;
import com.srinivasa.refrigerationworks.srw.repository.EmployeeRepository;
import com.srinivasa.refrigerationworks.srw.utility.common.PhoneNumberFormatter;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.UserStatus;
import com.srinivasa.refrigerationworks.srw.utility.mapper.EmployeeMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/*
 * Service to handle Employee-related operations.
 */
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    /*
     * Adds a new employee by converting the EmployeeDTO to an Employee entity,
     * formatting the phone number, saving it to the repository,
     * generating an employee ID, and returning it.
     */
    @Transactional
    public String addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.toEntity(employeeDTO);
        employee.setPhoneNumber(PhoneNumberFormatter.formatPhoneNumber(employee.getPhoneNumber()));
        employee.setStatus(UserStatus.ACTIVE);
        employeeRepository.save(employee);
        employee.setEmployeeId("SRW" + String.format("%04d", employee.getEmployeeReference()));
        employeeDTO.setEmployeeId(employee.getEmployeeId());
        return employee.getEmployeeId();
    }

    /*
     * Retrieves a list of all employees from the repository and maps them to EmployeeDTO objects.
     * Returns a list of EmployeeDTO to be used in other services or controllers.
     */
    public List<EmployeeDTO> getEmployeeList() {
        List<Employee> employees = employeeRepository.findAll();
        return employees
                .stream()
                .map(employeeMapper::toDto)
                .toList();
    }

    /*
     * Retrieves the employee details based on the provided identifier (phone number, email, national id number or employee ID).
     * If the identifier is a 10-digit phone number, it prefixes it with "+91".
     * Converts the employee entity to a DTO before returning.
     */
    public EmployeeDTO getEmployeeByIdentifier(String identifier) {
        identifier = identifier.matches("\\d{10}") ? PhoneNumberFormatter.formatPhoneNumber(identifier) : identifier;
        Employee employee = employeeRepository.findByIdentifier(identifier);
        return employeeMapper.toDto(employee);
    }

    /*
     * Updates employee details by mapping DTO to entity, formatting phone number,
     * and setting updated timestamp before saving to the repository.
     */
    public void updateEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.toEntity(employeeDTO);
        employee.setEmployeeId(employeeDTO.getEmployeeId());
        employee.setEmployeeReference(Long.parseLong(employeeDTO.getEmployeeId().substring(3,7)));
        String updatedPhoneNumber = PhoneNumberFormatter.formatPhoneNumber(employee.getPhoneNumber());
        employee.setPhoneNumber(updatedPhoneNumber);
        employee.setUpdatedAt(LocalDateTime.now());
        employeeRepository.save(employee);
    }

    /*
     * Deactivates an employee by updating their status to inactive.
     * - Sets the status to IN_ACTIVE and updates the timestamp for last updated and date of exit.
     */
    public void deactivateEmployee(String employeeId) {
        employeeRepository.deactivateEmployee(employeeId, LocalDateTime.now(), UserStatus.IN_ACTIVE);
    }
}