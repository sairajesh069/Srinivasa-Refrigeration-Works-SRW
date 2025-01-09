package com.srinivasa.refrigerationworks.srw.service;

import com.srinivasa.refrigerationworks.srw.entity.Employee;
import com.srinivasa.refrigerationworks.srw.payload.dto.EmployeeDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.EmployeeInfoDTO;
import com.srinivasa.refrigerationworks.srw.repository.EmployeeRepository;
import com.srinivasa.refrigerationworks.srw.utility.common.PhoneNumberFormatter;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.UserStatus;
import com.srinivasa.refrigerationworks.srw.utility.mapper.EmployeeMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/*
 * Service to handle Employee-related operations.
 */
@Service
@RequiredArgsConstructor
public class EmployeeService {

    /*
     * EmployeeRepository for performing CRUD operations on Employee entity.
     */
    private final EmployeeRepository employeeRepository;

    /*
     * EmployeeMapper for mapping EmployeeDTO to Employee entity and vice versa.
     */
    private final EmployeeMapper employeeMapper;

    /*
     * Adds a new employee, formats the phone number, saves the employee,
     * generates employee ID, and returns it.
     */
    @Transactional
    @CacheEvict(cacheNames = "employees", allEntries = true)
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
     * Retrieves a list of all employees and returns them as EmployeeDTO objects.
     */
    @Cacheable(value = "employees", key = "'employee_list'")
    public List<EmployeeDTO> getEmployeeList() {
        return employeeRepository
                .findAll()
                .stream()
                .map(employeeMapper::toDto)
                .toList();
    }

    /*
     * Retrieves a list of active employees and returns them as EmployeeDTO objects.
     */
    @Cacheable(value = "employees", key = "'active_employee_list'")
    public List<EmployeeDTO> getActiveEmployeeList() {
        return employeeRepository
                .findByStatus(UserStatus.ACTIVE)
                .stream()
                .map(employeeMapper::toDto)
                .toList();
    }

    /*
     * Retrieves employee details by identifier (phone number, email, national ID, or employee ID).
     * Formats phone number if it's a 10-digit number and returns EmployeeDTO.
     */
    @Cacheable(value = "employee", key = "'fetch-' + #identifier")
    public EmployeeDTO getEmployeeByIdentifier(String identifier) {
        Employee employee = employeeRepository.findByIdentifier(
                identifier.matches("\\d{10}") ? PhoneNumberFormatter.formatPhoneNumber(identifier) : identifier);
        return employeeMapper.toDto(employee);
    }

    /*
     * Updates employee details, formats phone number, and saves the changes.
     */
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "employees", allEntries = true),
                    @CacheEvict(cacheNames = "employee", key = "'fetch-' + #employeeDTO.employeeId")
            },
            put = @CachePut(value = "employee", key = "'update-' + #employeeDTO.employeeId")
    )
    public void updateEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.toEntity(employeeDTO);
        employee.setEmployeeId(employeeDTO.getEmployeeId());
        employee.setEmployeeReference(Long.parseLong(employeeDTO.getEmployeeId().substring(3, 7)));
        employee.setPhoneNumber(PhoneNumberFormatter.formatPhoneNumber(employee.getPhoneNumber()));
        employee.setUpdatedAt(LocalDateTime.now());
        employeeRepository.save(employee);
    }

    /*
     * Activates an employee by updating status to active.
     */
    @Caching(
            evict = @CacheEvict(cacheNames = {"employees", "techniciansInfo"}, allEntries = true),
            put = @CachePut(value = "employee", key = "'activate-' + #employeeId")
    )
    public void activateEmployee(String employeeId) {
        employeeRepository.updateEmployeeStatus(employeeId, LocalDateTime.now(), null, UserStatus.ACTIVE);
    }

    /*
     * Deactivates an employee by updating status to inactive.
     */
    @Caching(
            evict = @CacheEvict(cacheNames = {"employees", "techniciansInfo"}, allEntries = true),
            put = @CachePut(value = "employee", key = "'deactivate-' + #employeeId")
    )
    public void deactivateEmployee(String employeeId) {
        employeeRepository.updateEmployeeStatus(employeeId, LocalDateTime.now(), LocalDateTime.now(), UserStatus.IN_ACTIVE);
    }

    /*
     * Retrieves a list of EmployeeInfoDTOs for employees.
     */
    @Cacheable(value = "techniciansInfo", key = "#status + '_technicians_info'")
    public List<EmployeeInfoDTO> getEmployeesInfo() {
        return employeeRepository
                .findAll()
                .stream()
                .map(employee -> new EmployeeInfoDTO(
                        employee.getEmployeeId(),
                        employee.getFirstName() + " " + employee.getLastName(),
                        employee.getPhoneNumber(),
                        employee.getDesignation(),
                        employee.getStatus()))
                .toList();
    }
}