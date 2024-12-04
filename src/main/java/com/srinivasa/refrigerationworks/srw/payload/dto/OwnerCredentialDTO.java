package com.srinivasa.refrigerationworks.srw.payload.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * DTO combining Owner and User Credential details
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerCredentialDTO {

    /*
     * Contains validated owner details
     */
    @Valid
    private OwnerDTO ownerDTO;

    /*
     * Contains validated user credential details
     */
    @Valid
    private UserCredentialDTO userCredentialDTO;
}