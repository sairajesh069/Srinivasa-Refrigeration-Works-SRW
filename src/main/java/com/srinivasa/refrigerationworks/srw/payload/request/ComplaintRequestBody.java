package com.srinivasa.refrigerationworks.srw.payload.request;

import com.srinivasa.refrigerationworks.srw.payload.dto.ComplaintDTO;
import lombok.Data;

/*
 * Request body for ComplaintDTO data
 */
@Data
public class ComplaintRequestBody {

    /*
     * ComplaintDTO object with complaint details
     */
    private ComplaintDTO complaintDTO;
}