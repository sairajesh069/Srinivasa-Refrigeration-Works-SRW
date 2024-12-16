package com.srinivasa.refrigerationworks.srw.repository;

import com.srinivasa.refrigerationworks.srw.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/*
 * Repository interface for Customer entity
 * Provides CRUD operations using JpaRepository
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /*
     * Query to find a Customer entity based on the identifier.
     * The identifier can match the customer's ID, phone number, or email.
     */
    @Query("SELECT c FROM Customer c WHERE c.customerId = :identifier OR c.phoneNumber = :identifier OR c.email = :identifier")
    public Customer findByIdentifier(@Param("identifier") String identifier);
}