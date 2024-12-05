package com.srinivasa.refrigerationworks.srw.service;

import com.srinivasa.refrigerationworks.srw.entity.Employee;
import com.srinivasa.refrigerationworks.srw.payload.dto.EmployeeDTO;
import com.srinivasa.refrigerationworks.srw.repository.EmployeeRepository;
import com.srinivasa.refrigerationworks.srw.utility.mapper.EmployeeMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        employee.setPhoneNumber("+91" + employee.getPhoneNumber());
        employeeRepository.save(employee);
        employee.setEmployeeId("SRW" + String.format("%04d", employee.getEmployeeReference()));
        employeeDTO.setEmployeeId(employee.getEmployeeId());
        return employee.getEmployeeId();
    }
}
