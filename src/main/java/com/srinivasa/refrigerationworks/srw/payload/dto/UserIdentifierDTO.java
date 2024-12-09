package com.srinivasa.refrigerationworks.srw.payload.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * DTO for user identification.
 * Can represent userId, phoneNumber, or email for querying a user.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserIdentifierDTO {

    /*
     * Identifier representing userId, phoneNumber, or email (mandatory field).
     */
    @NotBlank(message = "Invalid input: Please provide a valid owner ID, phone number, or email")
    private String identifier;
}