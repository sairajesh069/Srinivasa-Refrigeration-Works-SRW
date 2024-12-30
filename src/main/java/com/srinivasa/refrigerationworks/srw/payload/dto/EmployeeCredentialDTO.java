package com.srinivasa.refrigerationworks.srw.payload.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * DTO combining Employee and User Credential details.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCredentialDTO {

    /*
     * Validated employee details.
     */
    @Valid
    private EmployeeDTO employeeDTO;

    /*
     * Validated user credential details.
     */
    @Valid
    private UserCredentialDTO userCredentialDTO;
}