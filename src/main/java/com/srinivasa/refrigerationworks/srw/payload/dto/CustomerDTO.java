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

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/*
 * DTO class for transferring Customer data with validation annotations.
 * Ensures that fields are validated for non-null, uniqueness, and correct formats.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@UniqueValue(fieldName = "phoneNumber", userIdField = "customerId", message = "This phone number is already registered")
@UniqueValue(fieldName = "email", userIdField = "customerId", message = "This email address is already registered")
public class CustomerDTO implements Serializable {

    /*
     * Unique ID for serialization compatibility.
     */
    @Serial
    private static final long serialVersionUID = 51L;

    /*
     * Customer's unique Id.
     */
    private String customerId;

    /*
     * Customer's first name (mandatory).
     */
    @NotNull(message = "First name is mandatory")
    private String firstName;

    /*
     * Customer's last name (mandatory).
     */
    @NotNull(message = "Last name is mandatory")
    private String lastName;

    /*
     * Customer's date of birth (mandatory, in the past).
     * Validates the format (yyyy-MM-dd).
     */
    @NotNull(message = "Date of Birth is mandatory")
    @Past(message = "Date of birth must be in the past.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    /*
     * Customer's gender (mandatory).
     */
    @NotNull(message = "Gender is mandatory")
    private String gender;

    /*
     * Customer's phone number (mandatory, valid format, unique).
     */
    @NotNull(message = "Phone number is mandatory")
    @Pattern(regexp = "^[0-9]{10}$|^\\+91[0-9]{10}$", message = "Please enter a valid phone number")
    private String phoneNumber;

    /*
     * Customer's email (mandatory, valid format, unique).
     */
    @NotNull(message = "Email is mandatory")
    @Email(message = "Please enter a valid email address")
    private String email;

    /*
     * Customer's address (mandatory).
     */
    @NotNull(message = "Address is mandatory")
    private String address;

    /*
     * Status of the customer.
     */
    private UserStatus status;
}