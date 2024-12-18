package com.srinivasa.refrigerationworks.srw.payload.response;

import com.srinivasa.refrigerationworks.srw.payload.dto.CustomerDTO;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.ResponseBodyStatus;
import lombok.Data;

/*
 * Response body class for returning CustomerDTO data along with a message and status
 */
@Data
public class CustomerResponseBody {

    /*
     * Message providing additional information about the response
     */
    private String message;

    /*
     * Status of the response (SUCCESS, FAILURE, ERROR, NOT_FOUND)
     */
    private ResponseBodyStatus status;

    /*
     * CustomerDTO object containing customer details
     */
    private CustomerDTO customerDTO;
}