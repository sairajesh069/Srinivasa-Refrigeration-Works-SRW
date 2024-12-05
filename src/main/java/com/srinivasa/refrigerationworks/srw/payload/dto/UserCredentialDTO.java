package com.srinivasa.refrigerationworks.srw.payload.dto;

import com.srinivasa.refrigerationworks.srw.entity.UserCredential;
import com.srinivasa.refrigerationworks.srw.validation.FieldMatch;
import com.srinivasa.refrigerationworks.srw.validation.UniqueValue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * DTO class for user credentials with validation annotations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/*
 * Validates that two fields match.
 * - Applies to 'password' and 'confirmPassword' fields.
 * - Displays a custom error message if they do not match.
 */
@FieldMatch(firstField = "password", secondField = "confirmPassword", message = "The passwords do not match. Please try again.")
public class UserCredentialDTO {

    /*
     * Username.
     * - Mandatory field.
     * - Must be at least 6 characters long.
     */
    @NotNull(message = "Username is required")
    @Size(min = 6, message = "Username must be at least 6 characters long")
    @UniqueValue(entityClass = UserCredential.class, fieldName = "username", message = "This username is already taken")
    private String username;

    /*
     * Password (mandatory field, must be at least 8 characters and match the pattern)
     */
    @NotNull(message = "Password is required")
    @Pattern(regexp = "^[a-zA-Z0-9@.#$&_]{8,}$|^\\$2[ayb]\\$.{56}$", message = "Password must be at least 8 characters long and can include digits, letters, and special characters (. @ # $ & _) only")
    private String password;

    /*
     * Confirm Password (mandatory field)
     */
    private String confirmPassword;
}
