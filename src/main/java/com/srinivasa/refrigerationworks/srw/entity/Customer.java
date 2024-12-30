package com.srinivasa.refrigerationworks.srw.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/*
 * Entity class representing the Customer in the database.
 */
@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
public class Customer implements Serializable {

    /*
     * Unique ID for serialization compatibility.
     */
    @Serial
    private static final long serialVersionUID = 50L;

    /*
     * Primary key for the customer entity, auto-generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_reference")
    private Long customerReference;

    /*
     * Customer's unique ID.
     */
    @Column(name = "customer_id")
    private String customerId;

    /*
     * Customer's first name.
     */
    @Column(name = "first_name")
    private String firstName;

    /*
     * Customer's last name.
     */
    @Column(name = "last_name")
    private String lastName;

    /*
     * Customer's date of birth.
     */
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    /*
     * Customer's gender.
     */
    @Column(name = "gender")
    private String gender;

    /*
     * Customer's unique phone number.
     */
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    /*
     * Customer's unique email.
     */
    @Column(name = "email", unique = true)
    private String email;

    /*
     * Customer's address.
     */
    @Column(name = "address")
    private String address;

    /*
     * Timestamp of when the customer record was created (not updatable).
     */
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "created_at", updatable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    /*
     * Timestamp of when the customer record was last updated.
     */
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /*
     * Status of the customer.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;
}