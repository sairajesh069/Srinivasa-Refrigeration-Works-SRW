package com.srinivasa.refrigerationworks.srw.payload.dto;

import com.srinivasa.refrigerationworks.srw.utility.common.enums.ComplaintState;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.ComplaintStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/*
 * DTO for transferring complaint data with validation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintDTO implements Serializable {

    /*
     * Serialization ID.
     */
    @Serial
    private static final long serialVersionUID = 61L;

    /*
     * Unique complaint ID.
     */
    private String complaintId;

    /*
     * ID of the person who booked the complaint.
     */
    private String bookedById;

    /*
     * Customer's name (mandatory).
     */
    @NotNull(message = "Name is mandatory")
    private String customerName;

    /*
     * Customer's contact number (mandatory, valid format).
     */
    @NotNull(message = "Contact number is mandatory")
    @Pattern(regexp = "^[0-9]{10}$|^\\+91[0-9]{10}$", message = "Please enter a valid phone number")
    private String contactNumber;

    /*
     * Customer's email.
     */
    private String email;

    /*
     * Customer's address (mandatory).
     */
    @NotNull(message = "Address is mandatory")
    private String address;

    /*
     * Product type (mandatory).
     */
    @NotNull(message = "Please select a valid product type")
    private String productType;

    /*
     * Product brand (mandatory).
     */
    @NotNull(message = "Please select a valid brand")
    private String brand;

    /*
     * Product model (mandatory).
     */
    @NotNull(message = "Please select a valid model")
    private String productModel;

    /*
     * Complaint description (mandatory).
     */
    @NotNull(message="Description is mandatory")
    private String description;

    /*
     * Complaint creation time.
     */
    private LocalDateTime createdAt;

    /*
     * Complaint status.
     */
    private ComplaintStatus status;

    /*
     * Last update time.
     */
    private LocalDateTime updatedAt;

    /*
     * Technician assigned to resolve the complaint.
     */
    private String technicianId;

    /*
     * Complaint closure time.
     */
    private LocalDateTime closedAt;

    /*
     * Customer feedback.
     */
    private String customerFeedback;

    /*
     * Complaint state.
     */
    private ComplaintState state;
}