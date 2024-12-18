package com.srinivasa.refrigerationworks.srw.payload.response;

import com.srinivasa.refrigerationworks.srw.payload.dto.ComplaintDTO;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.ResponseBodyStatus;
import lombok.Data;

/*
 * Response body class for returning ComplaintDTO data along with a message and status
 */
@Data
public class ComplaintResponseBody {

    /*
     * Message providing additional information about the response
     */
    private String message;

    /*
     * Status of the response (SUCCESS, FAILURE, ERROR, NOT_FOUND)
     */
    private ResponseBodyStatus status;

    /*
     * ComplaintDTO object containing complaint details
     */
    private ComplaintDTO complaintDTO;
}