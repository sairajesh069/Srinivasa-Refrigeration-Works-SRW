package com.srinivasa.refrigerationworks.srw.repository;

import com.srinivasa.refrigerationworks.srw.entity.UserCredential;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/*
 * Repository for UserCredential entity
 */
@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, String> {

    /*
     * Fetches username by phone number
     */
    @Query("SELECT username FROM UserCredential WHERE phoneNumber = :phoneNumber")
    public String fetchUsernameByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    /*
     * Checks if user exists by phone number and username
     */
    @Query("SELECT COUNT(u) > 0 FROM UserCredential u WHERE u.phoneNumber = :phoneNumber AND u.username = :username")
    public boolean existsByPhoneNumberAndUsername(@Param("phoneNumber") String phoneNumber, @Param("username") String username);

    /*
     * Updates user password by username
     */
    @Modifying
    @Transactional
    @Query("UPDATE UserCredential SET password = :password WHERE username = :username")
    public void updatePassword(@Param("username") String username, @Param("password") String password);

    /*
     * Finds user ID by username
     */
    @Query("SELECT userId FROM UserCredential WHERE username = :username")
    public String findUserIdByUsername(@Param("username") String username);

    /*
     * Updates user phone number by userId
     */
    @Modifying
    @Transactional
    @Query("UPDATE UserCredential SET phoneNumber = :phoneNumber WHERE userId = :userId")
    public void updateUserPhoneNumber(@Param("userId") String userId, @Param("phoneNumber") String phoneNumber);

    /*
     * Updates user status (enabled/disabled) by userId
     */
    @Modifying
    @Transactional
    @Query("UPDATE UserCredential SET enabled = :enabled WHERE userId = :userId")
    public void updateUserStatus(@Param("userId") String userId, @Param("enabled") byte enabled);
}