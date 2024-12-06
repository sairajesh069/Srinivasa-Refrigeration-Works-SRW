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
     * Checks if a customer with the given phone number exists in the database.
     */
    @Query("SELECT COUNT(c)>0 FROM Customer c WHERE c.phoneNumber = :phoneNumber")
    public boolean existsByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    /*
     * Checks if a customer with the given email exists in the database.
     */
    @Query("SELECT COUNT(c)>0 FROM Customer c WHERE c.email = :email")
    public boolean existsByEmail(@Param("email") String email);
}
