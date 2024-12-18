package com.srinivasa.refrigerationworks.srw.payload.request;

import com.srinivasa.refrigerationworks.srw.payload.dto.CustomerDTO;
import lombok.Data;

/*
 * Request body class for receiving CustomerDTO data
 */
@Data
public class CustomerRequestBody {

    /*
     * CustomerDTO object containing customer details
     */
    private CustomerDTO customerDTO;
}