package com.srinivasa.refrigerationworks.srw.utility.mapper;

import com.srinivasa.refrigerationworks.srw.entity.Employee;
import com.srinivasa.refrigerationworks.srw.payload.dto.EmployeeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/*
 * Mapper interface for converting between Employee entity and EmployeeDTO
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    /*
     * Converts EmployeeDTO to Employee entity
     * (ignores employeeReference, employeeId, dateOfHire, updatedAt, dateOfExit fields)
     */
    @Mapping(target = "employeeReference", ignore = true)
    @Mapping(target = "employeeId", ignore = true)
    @Mapping(target = "dateOfHire", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "dateOfExit", ignore = true)
    Employee toEntity(EmployeeDTO employeeDTO);

    /*
     * Converts Employee entity to EmployeeDTO
     */
    EmployeeDTO toDto(Employee employee);
}