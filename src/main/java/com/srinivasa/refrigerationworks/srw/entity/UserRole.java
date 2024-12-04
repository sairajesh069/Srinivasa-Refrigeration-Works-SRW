package com.srinivasa.refrigerationworks.srw.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Entity class representing user roles in the database
 */
@Entity
@Table(name = "user_roles")
@Data
@NoArgsConstructor
public class UserRole {

    /*
     * Composite primary key using UserRoleId
     */
    @EmbeddedId
    private UserRoleId userId;

    /*
     * Username associated with the role
     */
    @Column(name = "username")
    private String username;

    /*
     * Many-to-one relationship with UserCredential entity
     * Maps the userCredential ID from the embedded primary key
     */
    @ManyToOne
    @MapsId("userCredential")
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserCredential userCredential;

    /*
     * Constructor to initialize UserRole with username and role
     */
    public UserRole(String username, String role) {
        this.userId = new UserRoleId();
        this.username = username;
        this.userId.setRole(role);
    }
}

