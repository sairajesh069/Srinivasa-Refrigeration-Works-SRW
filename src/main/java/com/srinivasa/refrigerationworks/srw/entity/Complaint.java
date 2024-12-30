package com.srinivasa.refrigerationworks.srw.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.ComplaintState;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.ComplaintStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/*
 * Entity representing a complaint with customer and product details.
 */
@Entity
@Table(name="complaints")
@Data
@NoArgsConstructor
public class Complaint implements Serializable {

    /*
     * Serial version UID for compatibility.
     */
    @Serial
    private static final long serialVersionUID = 60L;

    /*
     * Auto-generated unique complaint reference ID.
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="complaint_reference")
    private Long complaintReference;

    /*
     * Unique complaint ID.
     */
    @Column(name="complaint_id", unique = true)
    private String complaintId;

    /*
     * ID of the person who booked the complaint.
     */
    @Column(name="booked_by_id")
    private String bookedById;

    /*
     * Customer name who raised the complaint.
     */
    @Column(name="customer_name")
    private String customerName;

    /*
     * Customer contact number.
     */
    @Column(name="contact_number")
    private String contactNumber;

    /*
     * Customer email.
     */
    @Column(name="email")
    private String email;

    /*
     * Customer address.
     */
    @Column(name="address")
    private String address;

    /*
     * Product type related to the complaint.
     */
    @Column(name = "product_type")
    private String productType;

    /*
     * Product brand related to the complaint.
     */
    @Column(name = "brand")
    private String brand;

    /*
     * Product model related to the complaint.
     */
    @Column(name = "product_model")
    private String productModel;

    /*
     * Description of the complaint.
     */
    @Column(name="description")
    private String description;

    /*
     * Complaint creation timestamp.
     */
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name="created_at", updatable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    /*
     * Complaint status (e.g., OPEN, IN_PROGRESS, RESOLVED).
     */
    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private ComplaintStatus status;

    /*
     * Timestamp for last complaint update.
     */
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    /*
     * Technician assigned to resolve the complaint.
     */
    @Column(name="technician_id")
    private String technicianId;

    /*
     * Complaint closure timestamp.
     */
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name="closed_at")
    private LocalDateTime closedAt;

    /*
     * Customer feedback after resolution.
     */
    @Column(name = "customer_feedback")
    private String customerFeedback;

    /*
     * Current state of the complaint.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private ComplaintState state;
}