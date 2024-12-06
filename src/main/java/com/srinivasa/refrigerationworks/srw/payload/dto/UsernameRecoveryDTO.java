package com.srinivasa.refrigerationworks.srw.payload.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Data Transfer Object (DTO) for Username Recovery.
 * Used to transfer phone number data for recovering the username.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsernameRecoveryDTO {

    /*
     * Phone number for username recovery (mandatory and validated with a regex pattern).
     */
    @NotNull(message = "Phone number is required")
    @Pattern(regexp = "^[0-9+]{10,13}$", message = "Please enter a valid phone number")
    private String phoneNumber;
}
