package com.srinivasa.refrigerationworks.srw.payload.request;

import com.srinivasa.refrigerationworks.srw.payload.dto.ComplaintDTO;
import lombok.Data;

/*
 * Request body class for receiving ComplaintDTO data
 */
@Data
public class ComplaintRequestBody {

    /*
     * ComplaintDTO object containing complaint details
     */
    private ComplaintDTO complaintDTO;
}