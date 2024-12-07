package com.srinivasa.refrigerationworks.srw.payload.dto;

import com.srinivasa.refrigerationworks.srw.utility.common.enums.ComplaintStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
 * Data Transfer Object (DTO) for transferring complaint-related data with validation annotations.
 * Used to transfer the details of a complaint such as customer information, product details, and status.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintDTO {

    /*
     * Unique identifier for the complaint.
     */
    private String complaintId;

    /*
     * ID of the person who booked the complaint.
     */
    private String bookedById;

    /*
     * Name of the customer who raised the complaint.
     * This field is mandatory.
     */
    @NotNull(message = "Name is mandatory")
    private String customerName;

    /*
     * Contact number of the customer.
     * This field is mandatory and must match a valid phone number pattern.
     */
    @NotNull(message = "Contact number is mandatory")
    @Pattern(regexp = "^[0-9+]{10,13}$", message = "Please enter a valid phone number")
    private String contactNumber;

    /*
     * Email address of the customer.
     * This field is mandatory and must be a valid email format.
     */
    @NotNull(message = "Email is mandatory")
    @Email(message = "Please enter a valid email address")
    private String email;

    /*
     * Address of the customer.
     * This field is mandatory.
     */
    @NotNull(message = "Address is mandatory")
    private String address;

    /*
     * Type of the product related to the complaint.
     * This field is mandatory.
     */
    @NotNull(message = "Please select a valid product type")
    private String productType;

    /*
     * Brand of the product related to the complaint.
     * This field is mandatory.
     */
    @NotNull(message = "Please select a valid brand")
    private String brand;

    /*
     * Model of the product related to the complaint.
     * This field is mandatory.
     */
    @NotNull(message = "Please select a valid model")
    private String productModel;

    /*
     * Detailed description of the complaint.
     * This field is mandatory.
     */
    @NotNull(message="Description is mandatory")
    private String description;

    /*
     * Status of the complaint (e.g., OPEN, IN_PROGRESS, RESOLVED).
     */
    private ComplaintStatus status;

    /*
     * Date and time when the complaint was last updated.
     */
    private LocalDateTime updatedAt;

    /*
     * Technician assigned to resolve the complaint.
     */
    private String technicianId;

    /*
     * Date and time when the complaint was closed.
     * This field is populated when the complaint is resolved.
     */
    private LocalDateTime closedAt;

    /*
     * Customer feedback after the resolution of the complaint.
     */
    private String customerFeedback;
}
