package com.srinivasa.refrigerationworks.srw.payload.request;

import com.srinivasa.refrigerationworks.srw.payload.dto.CustomerDTO;
import lombok.Data;

/*
 * Request body for CustomerDTO data
 */
@Data
public class CustomerRequestBody {

    /*
     * CustomerDTO object with customer details
     */
    private CustomerDTO customerDTO;
}