package com.srinivasa.refrigerationworks.srw.repository;

import com.srinivasa.refrigerationworks.srw.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * Repository interface for Owner entity
 * Provides CRUD operations using JpaRepository
 */
@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
