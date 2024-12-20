package com.srinivasa.refrigerationworks.srw.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/*
 * Entity class representing the Employee in the database
 */
@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
public class Employee {

    /*
     * Primary key for Employee entity
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_reference")
    private Long employeeReference;

    /*
     * Unique Employee ID
     */
    @Column(name = "employee_id")
    private String employeeId;

    /*
     * Employee's first name
     */
    @Column(name = "first_name")
    private String firstName;

    /*
     * Employee's last name
     */
    @Column(name = "last_name")
    private String lastName;

    /*
     * Employee's date of birth
     */
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    /*
     * Employee's gender
     */
    @Column(name = "gender")
    private String gender;

    /*
     * Employee's unique phone number
     */
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    /*
     * Employee's unique email address
     */
    @Column(name = "email", unique = true)
    private String email;

    /*
     * Employee's address
     */
    @Column(name = "address")
    private String address;

    /*
     * Unique national ID number of the employee
     */
    @Column(name = "national_id_number", unique = true)
    private String nationalIdNumber;

    /*
     * Employee's job designation
     */
    @Column(name = "designation")
    private String designation;

    /*
     * Date and time of hire (defaulted to current timestamp)
     * Cannot be updated after creation.
     */
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "date_of_hire", updatable = false)
    private final LocalDateTime dateOfHire = LocalDateTime.now();

    /*
     * Employee's salary
     */
    @Column(name = "salary")
    private Long salary;

    /*
     * Timestamp of when the employee record was last updated
     */
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /*
     * Date and time of exit, if applicable
     */
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "date_of_exit")
    private LocalDateTime dateOfExit;

    /*
     * Status of the employee
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;
}