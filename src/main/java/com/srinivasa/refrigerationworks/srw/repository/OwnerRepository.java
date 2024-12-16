package com.srinivasa.refrigerationworks.srw.repository;

import com.srinivasa.refrigerationworks.srw.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}