package com.srinivasa.refrigerationworks.srw.repository;

import com.srinivasa.refrigerationworks.srw.entity.UserCredential;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    /*
     * Checks if a UserCredential exists with the given phone number and username.
     * - Returns true if a matching record exists.
     */
    @Query("SELECT COUNT(u) > 0 FROM UserCredential u WHERE u.phoneNumber = :phoneNumber AND u.username = :username")
    public boolean existsByPhoneNumberAndUsername(@Param("phoneNumber") String phoneNumber, @Param("username") String username);

    /*
     * Updates the password for a UserCredential identified by the username.
     * - The operation is transactional and modifying.
     */
    @Modifying
    @Transactional
    @Query("UPDATE UserCredential SET password = :password WHERE username = :username")
    public void updatePassword(@Param("username") String username, @Param("password") String password);

    /*
     * Custom query to find the user ID associated with a given username.
     * - Queries the UserCredential table to retrieve the user ID for the provided username.
     */
    @Query("SELECT userId FROM UserCredential WHERE username = :username")
    public String findUserIdByUsername(@Param("username") String username);

    /*
     * Custom query to fetch the user type (role) for a given username from the UserCredential table.
     */
    @Query("SELECT userType FROM UserCredential WHERE username = :username")
    public String findUserTypeByUsername(@Param("username") String username);
}
