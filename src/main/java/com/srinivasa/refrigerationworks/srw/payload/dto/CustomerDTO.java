package com.srinivasa.refrigerationworks.srw.payload.dto;

import com.srinivasa.refrigerationworks.srw.entity.Customer;
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
 * DTO class for transferring Customer data with validation annotations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    /*
     * Customer's unique Id
     */
    private String customerId;

    /*
     * Customer's first name (mandatory field)
     */
    @NotNull(message = "First name is mandatory")
    private String firstName;

    /*
     * Customer's last name (mandatory field)
     */
    @NotNull(message = "Last name is mandatory")
    private String lastName;

    /*
     * Customer's date of birth
     * - NotNull: Ensures that the date of birth is provided.
     * - Past: Ensures that the date of birth is in the past.
     * - DateTimeFormat: Validates the date format (yyyy-MM-dd).
     */
    @NotNull(message = "Date of Birth is mandatory")
    @Past(message = "Date of birth must be in the past.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    /*
     * Customer's gender (mandatory field)
     */
    @NotNull(message = "Gender is mandatory")
    private String gender;

    /*
     * Customer's phone number.
     * - Mandatory field.
     * - Must match the specified regex for valid phone numbers.
     * - Ensures uniqueness across all user-related entities.
     */
    @NotNull(message = "Phone number is mandatory")
    @Pattern(regexp = "^[0-9+]{10,13}$", message = "Please enter a valid phone number")
    @UniqueValue(fieldName = "phoneNumber", entityClass = Customer.class, inEveryUserEntity = true, message = "This phone number is already registered")
    private String phoneNumber;

    /*
     * Customer's email address.
     * - Mandatory field.
     * - Must be a valid email format.
     * - Ensures uniqueness across all user-related entities.
     */
    @NotNull(message = "Email is mandatory")
    @Email(message = "Please enter a valid email address")
    @UniqueValue(fieldName = "email", entityClass = Customer.class, inEveryUserEntity = true, message = "This email address is already registered")
    private String email;

    /*
     * Customer's address (mandatory field)
     */
    @NotNull(message = "Address is mandatory")
    private String address;
}
