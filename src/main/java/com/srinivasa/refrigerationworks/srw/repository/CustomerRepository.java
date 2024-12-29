package com.srinivasa.refrigerationworks.srw.repository;

import com.srinivasa.refrigerationworks.srw.entity.Customer;
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

    /*
     * Fetches a list of customers based on their status.
     */
    public List<Customer> findByStatus(UserStatus status);

    /*
     * Activates/Deactivates a customer by updating their status and 'updatedAt' fields.
     * - Updates the Customer entity for the specified customerId.
     */
    @Modifying
    @Transactional
    @Query("UPDATE Customer SET updatedAt = :updatedAt, status = :status WHERE customerId = :customerId")
    public void updateCustomerStatus(@Param("customerId") String customerId, @Param("updatedAt") LocalDateTime updatedAt, @Param("status") UserStatus status);
}