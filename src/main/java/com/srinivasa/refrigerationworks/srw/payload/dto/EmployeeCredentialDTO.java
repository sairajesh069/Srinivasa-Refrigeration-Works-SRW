package com.srinivasa.refrigerationworks.srw.payload.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * DTO combining Employee and User Credential details
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCredentialDTO {

    /*
     * Contains validated employee details
     */
    @Valid
    private EmployeeDTO employeeDTO;

    /*
     * Contains validated user credential details
     */
    @Valid
    private UserCredentialDTO userCredentialDTO;
}