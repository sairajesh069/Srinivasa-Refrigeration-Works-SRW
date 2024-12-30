package com.srinivasa.refrigerationworks.srw.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/*
 * Embedded class for composite primary key (UserCredential and Role).
 */
@Embeddable
@Data
public class UserRoleId implements Serializable {

    /*
     * Unique ID for serialization compatibility.
     */
    @Serial
    private static final long serialVersionUID = 21L;

    /*
     * User identifier (references UserCredential).
     */
    @Column(name = "user_id")
    private String userCredential;

    /*
     * Role assigned to the user.
     */
    @Column(name = "roles")
    private String role;
}