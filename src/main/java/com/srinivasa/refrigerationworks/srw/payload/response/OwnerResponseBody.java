package com.srinivasa.refrigerationworks.srw.payload.response;

import com.srinivasa.refrigerationworks.srw.payload.dto.OwnerDTO;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.ResponseBodyStatus;
import lombok.Data;

/*
 * Response body for OwnerDTO data with message and status
 */
@Data
public class OwnerResponseBody {

    /*
     * Message with additional response information
     */
    private String message;

    /*
     * Status of the response (SUCCESS, FAILURE, ERROR, NOT_FOUND)
     */
    private ResponseBodyStatus status;

    /*
     * OwnerDTO object with owner details
     */
    private OwnerDTO ownerDTO;
}