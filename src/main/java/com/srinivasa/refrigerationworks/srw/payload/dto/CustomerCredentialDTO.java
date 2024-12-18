package com.srinivasa.refrigerationworks.srw.payload.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * DTO combining Customer and User Credential details
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCredentialDTO {

    /*
     * Contains validated customer details
     */
    @Valid
    private CustomerDTO customerDTO;

    /*
     * Contains validated user credential details
     */
    @Valid
    private UserCredentialDTO userCredentialDTO;
}