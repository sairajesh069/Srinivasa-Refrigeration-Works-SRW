package com.srinivasa.refrigerationworks.srw.payload.response;

import com.srinivasa.refrigerationworks.srw.payload.dto.CustomerDTO;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.ResponseBodyStatus;
import lombok.Data;

/*
 * Response body for CustomerDTO data with message and status
 */
@Data
public class CustomerResponseBody {

    /*
     * Message with additional response information
     */
    private String message;

    /*
     * Status of the response (SUCCESS, FAILURE, ERROR, NOT_FOUND)
     */
    private ResponseBodyStatus status;

    /*
     * CustomerDTO object with customer details
     */
    private CustomerDTO customerDTO;
}