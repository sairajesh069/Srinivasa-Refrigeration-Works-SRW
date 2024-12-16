package com.srinivasa.refrigerationworks.srw.entity;

import com.srinivasa.refrigerationworks.srw.utility.common.enums.UserType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/*
 * Entity class for storing user credentials in the database
 */
@Entity
@Table(name = "user_credentials")
@Data
@NoArgsConstructor
public class UserCredential {

    /*
     * Unique user identifier
     */
    @Id
    @Column(name = "user_id", unique = true)
    private String userId;

    /*
     * Unique phone number for the user
     */
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    /*
     * Unique username for the user
     */
    @Column(name = "username", unique = true)
    private String username;

    /*
     * Encrypted password for the user
     */
    @Column(name = "password")
    private String password;

    /*
     * Confirm password (not persisted in the database)
     */
    @Transient
    private String confirmPassword;

    /*
     * Indicates whether the user account is enabled (active)
     */
    @Column(name = "enabled")
    private short enabled;

    /*
     * User's type (role) from the UserType enum
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    /*
     * List of roles assigned to the user.
     * Excluded from toString method.
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "userCredential", cascade = CascadeType.ALL)
    private List<UserRole> userRoles;

    /*
     * Method triggered before the UserCredential entity is persisted
     * Sets the enabled field to 1 and confirms the password
     */
    @PrePersist
    public void prePersist() {
        this.enabled = 1;
        this.confirmPassword = password;
        this.phoneNumber = phoneNumber.startsWith("+91") ? phoneNumber : "+91" + phoneNumber;
    }

    /*
     * Method to add a user role to the user
     */
    public void addUserRole(UserRole userRole) {
        if (userRoles == null) {
            userRoles = new ArrayList<>();
        }
        userRoles.add(userRole);
        userRole.setUserCredential(this);
    }
}