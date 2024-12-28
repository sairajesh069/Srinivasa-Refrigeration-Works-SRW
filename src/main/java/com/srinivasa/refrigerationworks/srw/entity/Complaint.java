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
 * Entity class representing a complaint in the system.
 * It contains details about the complaint such as the customer, product, status, and feedback.
 */
@Entity
@Table(name="complaints")
@Data
@NoArgsConstructor
public class Complaint implements Serializable {

    /*
     * Unique ID for serialization compatibility.
     */
    @Serial
    private static final long serialVersionUID = 60L;

    /*
     * Unique identifier for the complaint (generated automatically).
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
     * Name of the customer who raised the complaint.
     */
    @Column(name="customer_name")
    private String customerName;

    /*
     * Contact number of the customer.
     */
    @Column(name="contact_number")
    private String contactNumber;

    /*
     * Email of the customer.
     */
    @Column(name="email")
    private String email;

    /*
     * Address of the customer.
     */
    @Column(name="address")
    private String address;

    /*
     * Type of the product related to the complaint.
     */
    @Column(name = "product_type")
    private String productType;

    /*
     * Brand of the product related to the complaint.
     */
    @Column(name = "brand")
    private String brand;

    /*
     * Model of the product related to the complaint.
     */
    @Column(name = "product_model")
    private String productModel;

    /*
     * Detailed description of the complaint.
     */
    @Column(name="description")
    private String description;

    /*
     * Date and time when the complaint was created.
     * It is set automatically to the current timestamp and is not updated.
     */
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name="created_at", updatable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    /*
     * Status of the complaint (e.g., OPEN, IN_PROGRESS, RESOLVED).
     * Default is OPEN when a complaint is created.
     */
    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private ComplaintStatus status;

    /*
     * Date and time when the complaint was last updated.
     * This field is updated manually as the complaint progresses.
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
     * Date and time when the complaint was closed.
     * This is only set when the complaint is marked as resolved.
     */
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name="closed_at")
    private LocalDateTime closedAt;

    /*
     * Customer feedback about the resolution or handling of the complaint.
     */
    @Column(name = "customer_feedback")
    private String customerFeedback;

    /*
     * State of the complaint
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private ComplaintState state;
}