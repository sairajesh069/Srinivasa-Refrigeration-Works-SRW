package com.srinivasa.refrigerationworks.srw.payload.request;

import com.srinivasa.refrigerationworks.srw.payload.dto.OwnerDTO;
import lombok.Data;

/*
 * Request body for OwnerDTO data
 */
@Data
public class OwnerRequestBody {

    /*
     * OwnerDTO object with owner details
     */
    private OwnerDTO ownerDTO;
}