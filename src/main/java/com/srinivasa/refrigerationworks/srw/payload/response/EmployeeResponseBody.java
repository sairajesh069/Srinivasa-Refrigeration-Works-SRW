package com.srinivasa.refrigerationworks.srw.payload.response;

import com.srinivasa.refrigerationworks.srw.payload.dto.EmployeeDTO;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.ResponseBodyStatus;
import lombok.Data;

/*
 * Response body class for returning EmployeeDTO data along with a message and status
 */
@Data
public class EmployeeResponseBody {

    /*
     * Message providing additional information about the response
     */
    private String message;

    /*
     * Status of the response (SUCCESS, FAILURE, ERROR, NOT_FOUND)
     */
    private ResponseBodyStatus Status;

    /*
     * EmployeeDTO object containing employee details
     */
    private EmployeeDTO employeeDTO;
}