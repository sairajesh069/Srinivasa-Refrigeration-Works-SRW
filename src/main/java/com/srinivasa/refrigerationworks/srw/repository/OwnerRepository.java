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
     * Checks if an owner with the given phone number exists in the database.
     */
    @Query("SELECT COUNT(o)>0 FROM Owner o WHERE o.phoneNumber = :phoneNumber")
    public boolean existsByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    /*
     * Checks if an owner with the given email exists in the database.
     */
    @Query("SELECT COUNT(o)>0 FROM Owner o WHERE o.email = :email")
    public boolean existsByEmail(@Param("email") String email);

    /*
     * Query to find an Owner entity based on the identifier.
     * The identifier can match the owner's ID, phone number, or email.
     */
    @Query("SELECT o FROM Owner o WHERE o.ownerId = :identifier OR o.phoneNumber = :identifier OR o.email = :identifier")
    public Owner findByIdentifier(@Param("identifier") String identifier);
}
