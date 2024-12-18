package com.srinivasa.refrigerationworks.srw.payload.request;

import com.srinivasa.refrigerationworks.srw.payload.dto.OwnerDTO;
import lombok.Data;

/*
 * Request body class for receiving OwnerDTO data
 */
@Data
public class OwnerRequestBody {

    /*
     * OwnerDTO object containing owner details
     */
    private OwnerDTO ownerDTO;
}