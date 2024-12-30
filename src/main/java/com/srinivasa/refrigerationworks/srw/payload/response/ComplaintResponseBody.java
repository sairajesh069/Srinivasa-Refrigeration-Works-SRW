package com.srinivasa.refrigerationworks.srw.payload.response;

import com.srinivasa.refrigerationworks.srw.payload.dto.ComplaintDTO;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.ResponseBodyStatus;
import lombok.Data;

/*
 * Response body for ComplaintDTO data with message and status
 */
@Data
public class ComplaintResponseBody {

    /*
     * Message with additional response information
     */
    private String message;

    /*
     * Status of the response (SUCCESS, FAILURE, ERROR, NOT_FOUND)
     */
    private ResponseBodyStatus status;

    /*
     * ComplaintDTO object with complaint details
     */
    private ComplaintDTO complaintDTO;
}