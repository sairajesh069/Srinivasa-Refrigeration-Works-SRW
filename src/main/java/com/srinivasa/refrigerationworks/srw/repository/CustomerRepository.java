package com.srinivasa.refrigerationworks.srw.repository;

import com.srinivasa.refrigerationworks.srw.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * Repository interface for Customer entity
 * Provides CRUD operations using JpaRepository
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
