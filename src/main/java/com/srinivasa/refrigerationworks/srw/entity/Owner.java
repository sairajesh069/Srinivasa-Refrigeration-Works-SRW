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
 * Entity class representing the Owner in the database.
 */
@Entity
@Table(name = "owners")
@Data
@NoArgsConstructor
public class Owner implements Serializable {

    /*
     * Unique ID for serialization compatibility.
     */
    @Serial
    private static final long serialVersionUID = 30L;

    /*
     * Primary key for the owner entity, auto-generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owner_reference", unique = true)
    private Long ownerReference;

    /*
     * Owner's unique ID.
     */
    @Column(name = "owner_id", unique = true)
    private String ownerId;

    /*
     * Owner's first name.
     */
    @Column(name = "first_name")
    private String firstName;

    /*
     * Owner's last name.
     */
    @Column(name = "last_name")
    private String lastName;

    /*
     * Owner's date of birth.
     */
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    /*
     * Owner's gender.
     */
    @Column(name = "gender")
    private String gender;

    /*
     * Owner's unique phone number.
     */
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    /*
     * Owner's unique email.
     */
    @Column(name = "email", unique = true)
    private String email;

    /*
     * Owner's address.
     */
    @Column(name = "address")
    private String address;

    /*
     * Timestamp of when the owner record was created (not updatable).
     */
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "created_at", updatable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    /*
     * Timestamp of when the owner record was last updated.
     */
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /*
     * Status of the owner.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;
}