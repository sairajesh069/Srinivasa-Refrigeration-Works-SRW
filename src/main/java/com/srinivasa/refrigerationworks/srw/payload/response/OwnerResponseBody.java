package com.srinivasa.refrigerationworks.srw.payload.response;

import com.srinivasa.refrigerationworks.srw.payload.dto.OwnerDTO;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.ResponseBodyStatus;
import lombok.Data;

/*
 * Response body class for returning OwnerDTO data along with a message and status
 */
@Data
public class OwnerResponseBody {

    /*
     * Message providing additional information about the response
     */
    private String message;

    /*
     * Status of the response (SUCCESS, FAILURE, ERROR, NOT_FOUND)
     */
    private ResponseBodyStatus status;

    /*
     * OwnerDTO object containing owner details
     */
    private OwnerDTO ownerDTO;
}