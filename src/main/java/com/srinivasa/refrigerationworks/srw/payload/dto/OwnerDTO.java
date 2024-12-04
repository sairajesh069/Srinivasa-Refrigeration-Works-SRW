package com.srinivasa.refrigerationworks.srw.payload.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/*
 * DTO class for transferring Owner data with validation annotations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
     * Owner's date of birth (mandatory field)
     */
    @NotNull(message = "Date of Birth is mandatory")
    private LocalDate dateOfBirth;

    /*
     * Owner's gender (mandatory field)
     */
    @NotNull(message = "Gender is mandatory")
    private String gender;

    /*
     * Owner's phone number (mandatory field, must match specified regex)
     */
    @NotNull(message = "Phone number is mandatory")
    @Pattern(regexp = "^[0-9+]{10,13}$", message = "Please enter a valid phone number")
    private String phoneNumber;

    /*
     * Owner's email (mandatory field, must be a valid email)
     */
    @NotNull(message = "Email is mandatory")
    @Email(message = "Please enter a valid email address")
    private String email;

    /*
     * Owner's address (mandatory field)
     */
    @NotNull(message = "Address is mandatory")
    private String address;
}
