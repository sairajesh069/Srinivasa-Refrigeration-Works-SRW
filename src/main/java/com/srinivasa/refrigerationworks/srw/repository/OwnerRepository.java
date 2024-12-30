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
import java.util.List;

/*
 * Repository for Owner entity
 */
@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    /*
     * Finds owner by ID, phone, or email
     */
    @Query("SELECT o FROM Owner o WHERE o.ownerId = :identifier OR o.phoneNumber = :identifier OR o.email = :identifier")
    public Owner findByIdentifier(@Param("identifier") String identifier);

    /*
     * Retrieves owners by status
     */
    public List<Owner> findByStatus(UserStatus status);

    /*
     * Updates owner status and 'updatedAt'
     */
    @Modifying
    @Transactional
    @Query("UPDATE Owner SET updatedAt = :updatedAt, status = :status WHERE ownerId = :ownerId")
    public void updateOwnerStatus(@Param("ownerId") String ownerId, @Param("updatedAt") LocalDateTime updatedAt, @Param("status") UserStatus status);
}