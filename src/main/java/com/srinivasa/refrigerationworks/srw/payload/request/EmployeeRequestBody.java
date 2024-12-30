package com.srinivasa.refrigerationworks.srw.payload.request;

import com.srinivasa.refrigerationworks.srw.payload.dto.EmployeeDTO;
import lombok.Data;

/*
 * Request body for EmployeeDTO data
 */
@Data
public class EmployeeRequestBody {

    /*
     * EmployeeDTO object with employee details
     */
    private EmployeeDTO employeeDTO;
}