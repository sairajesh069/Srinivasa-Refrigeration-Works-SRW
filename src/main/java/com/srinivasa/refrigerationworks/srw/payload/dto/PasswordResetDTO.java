package com.srinivasa.refrigerationworks.srw.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * DTO for password reset, containing user info and new password details.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetDTO {

    /*
     * User's phone number for identification.
     */
    private String phoneNumber;

    /*
     * Username for validation.
     */
    private String username;

    /*
     * New password for the user.
     */
    private String password;

    /*
     * Password confirmation (must match new password).
     */
    private String confirmPassword;
}