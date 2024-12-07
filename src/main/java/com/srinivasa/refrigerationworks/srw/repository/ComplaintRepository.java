package com.srinivasa.refrigerationworks.srw.repository;

import com.srinivasa.refrigerationworks.srw.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * Repository interface for Complaint entity
 * Provides CRUD operations using JpaRepository
 */
@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
}