package com.srinivasa.refrigerationworks.srw.payload.response;

import com.srinivasa.refrigerationworks.srw.payload.dto.EmployeeDTO;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.ResponseBodyStatus;
import lombok.Data;

/*
 * Response body for EmployeeDTO data with message and status
 */
@Data
public class EmployeeResponseBody {

    /*
     * Message with additional response information
     */
    private String message;

    /*
     * Status of the response (SUCCESS, FAILURE, ERROR, NOT_FOUND)
     */
    private ResponseBodyStatus status;

    /*
     * EmployeeDTO object with employee details
     */
    private EmployeeDTO employeeDTO;
}