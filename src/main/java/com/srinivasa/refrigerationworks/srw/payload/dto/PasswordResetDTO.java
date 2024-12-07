package com.srinivasa.refrigerationworks.srw.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Data Transfer Object (DTO) for Password Reset.
 * Used to transfer data required for resetting a user's password.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetDTO {

    /*
     * The phone number of the user.
     * Used to identify the user for password reset.
     */
    private String phoneNumber;

    /*
     * The username of the user.
     * Used to validate the user during password reset.
     */
    private String username;

    /*
     * The new password for the user.
     * Must meet defined security criteria.
     */
    private String password;

    /*
     * Confirmation of the new password.
     * Must match the password field.
     */
    private String confirmPassword;
}