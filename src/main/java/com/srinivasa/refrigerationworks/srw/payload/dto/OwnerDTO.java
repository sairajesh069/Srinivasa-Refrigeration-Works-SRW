package com.srinivasa.refrigerationworks.srw.payload.dto;

import com.srinivasa.refrigerationworks.srw.utility.common.enums.UserStatus;
import com.srinivasa.refrigerationworks.srw.validation.UniqueValue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/*
 * DTO class for transferring Owner data with validation annotations.
 * Ensures that fields are validated for non-null, uniqueness, and correct formats.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@UniqueValue(fieldName = "phoneNumber", userIdField = "ownerId", message = "This phone number is already registered")
@UniqueValue(fieldName = "email", userIdField = "ownerId", message = "This email address is already registered")
public class OwnerDTO {

    /*
     * Owner's unique Id
     */
    private String ownerId;

    /*
     * Owner's first name (mandatory field)
     */
    @NotNull(message = "First name is mandatory")
    private String firstName;

    /*
     * Owner's last name (mandatory field)
     */
    @NotNull(message = "Last name is mandatory")
    private String lastName;

    /*
     * Owner's date of birth
     * - NotNull: Ensures that the date of birth is provided.
     * - Past: Ensures that the date of birth is in the past.
     * - DateTimeFormat: Validates the date format (yyyy-MM-dd).
     */
    @NotNull(message = "Date of Birth is mandatory")
    @Past(message = "Date of birth must be in the past.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    /*
     * Owner's gender (mandatory field)
     */
    @NotNull(message = "Gender is mandatory")
    private String gender;

    /*
     * Owner's phone number.
     * - Mandatory field.
     * - Must match the specified regex for valid phone numbers.
     * - Ensures uniqueness across all user-related entities.
     */
    @NotNull(message = "Phone number is mandatory")
    @Pattern(regexp = "^[0-9+]{10,13}$", message = "Please enter a valid phone number")
    private String phoneNumber;

    /*
     * Owner's email address.
     * - Mandatory field.
     * - Must be a valid email format.
     * - Ensures uniqueness across all user-related entities.
     */
    @NotNull(message = "Email is mandatory")
    @Email(message = "Please enter a valid email address")
    private String email;

    /*
     * Owner's address (mandatory field)
     */
    @NotNull(message = "Address is mandatory")
    private String address;

    /*
     * Status of the owner
     */
    private UserStatus status;
}