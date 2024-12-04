package com.srinivasa.refrigerationworks.srw.repository;

import com.srinivasa.refrigerationworks.srw.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * Repository interface for UserCredential entity
 * Provides CRUD operations using JpaRepository
 */
@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, String> {
}
