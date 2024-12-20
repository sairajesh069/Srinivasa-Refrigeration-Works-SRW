package com.srinivasa.refrigerationworks.srw.repository;

import com.srinivasa.refrigerationworks.srw.entity.Owner;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.UserStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/*
 * Repository interface for Owner entity
 * Provides CRUD operations using JpaRepository
 */
@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    /*
     * Query to find an Owner entity based on the identifier.
     * The identifier can match the owner's ID, phone number, or email.
     */
    @Query("SELECT o FROM Owner o WHERE o.ownerId = :identifier OR o.phoneNumber = :identifier OR o.email = :identifier")
    public Owner findByIdentifier(@Param("identifier") String identifier);

    /*
     * Deactivates an owner by updating their status and 'updatedAt' field.
     * - Updates the Owner entity for the specified ownerId.
     */
    @Modifying
    @Transactional
    @Query("UPDATE Owner SET updatedAt = :updatedAt, status = :status WHERE ownerId = :ownerId")
    public void deactivateOwner(@Param("ownerId") String ownerId, @Param("updatedAt") LocalDateTime updatedAt, @Param("status") UserStatus status);
}