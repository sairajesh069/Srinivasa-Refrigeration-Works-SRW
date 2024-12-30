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

import java.io.Serial;
import java.io.Serializable;

/*
 * DTO for user credentials with validation annotations for username and password.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldMatch(firstField = "password", secondField = "confirmPassword", message = "Passwords do not match.")
@UniqueValue(fieldName = "username", entityClass = UserCredential.class, inEveryUserEntity = false, userIdField = "userId", message = "Username already taken.")
public class UserCredentialDTO implements Serializable {

    /*
     * Serialization ID.
     */
    @Serial
    private static final long serialVersionUID = 11L;

    /*
     * User's unique ID.
     */
    private String userId;

    /*
     * Username (min 6 chars, unique).
     */
    @NotNull(message = "Username is required")
    @Size(min = 6, message = "Username must be at least 6 characters.")
    private String username;

    /*
     * Password (min 8 chars, regex validation).
     */
    @NotNull(message = "Password is required")
    @Pattern(regexp = "^[a-zA-Z0-9@.#$&_]{8,}$|^\\$2[ayb]\\$.{56}$", message = "Password must be at least 8 characters, with allowed symbols.")
    private String password;

    /*
     * Confirm password (must match password).
     */
    private String confirmPassword;
}