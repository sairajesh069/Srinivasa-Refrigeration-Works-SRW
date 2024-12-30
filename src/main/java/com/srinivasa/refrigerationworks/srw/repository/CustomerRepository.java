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
 * Repository for Customer entity
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /*
     * Finds customer by ID, phone, or email
     */
    @Query("SELECT c FROM Customer c WHERE c.customerId = :identifier OR c.phoneNumber = :identifier OR c.email = :identifier")
    public Customer findByIdentifier(@Param("identifier") String identifier);

    /*
     * Retrieves customers by status
     */
    public List<Customer> findByStatus(UserStatus status);

    /*
     * Updates customer status and 'updatedAt'
     */
    @Modifying
    @Transactional
    @Query("UPDATE Customer SET updatedAt = :updatedAt, status = :status WHERE customerId = :customerId")
    public void updateCustomerStatus(@Param("customerId") String customerId, @Param("updatedAt") LocalDateTime updatedAt, @Param("status") UserStatus status);
}