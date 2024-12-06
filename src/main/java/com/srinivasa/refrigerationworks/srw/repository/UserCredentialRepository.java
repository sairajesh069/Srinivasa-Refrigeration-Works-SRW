package com.srinivasa.refrigerationworks.srw.repository;

import com.srinivasa.refrigerationworks.srw.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/*
 * Repository interface for UserCredential entity
 * Provides CRUD operations using JpaRepository
 */
@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, String> {

    /*
     * Query to fetch the username associated with the given phone number.
     * - Returns the username as a string.
     */
    @Query("SELECT username FROM UserCredential WHERE phoneNumber = :phoneNumber")
    public String fetchUsernameByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}
