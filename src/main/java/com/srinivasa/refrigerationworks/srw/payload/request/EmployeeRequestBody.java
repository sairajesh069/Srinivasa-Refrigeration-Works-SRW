package com.srinivasa.refrigerationworks.srw.payload.request;

import com.srinivasa.refrigerationworks.srw.payload.dto.EmployeeDTO;
import lombok.Data;

/*
 * Request body class for receiving EmployeeDTO data
 */
@Data
public class EmployeeRequestBody {

    /*
     * EmployeeDTO object containing employee details
     */
    private EmployeeDTO employeeDTO;
}
