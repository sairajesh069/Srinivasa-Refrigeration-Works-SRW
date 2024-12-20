package com.srinivasa.refrigerationworks.srw.repository;

import com.srinivasa.refrigerationworks.srw.entity.Employee;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.UserStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/*
 * Repository interface for Employee entity
 * Provides CRUD operations using JpaRepository
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /*
     * Query to find an Employee entity based on the identifier.
     * The identifier can match the employee's ID, phone number, national id number or email.
     */
    @Query("SELECT e FROM Employee e WHERE e.employeeId = :identifier OR e.phoneNumber = :identifier OR e.email = :identifier or e.nationalIdNumber = :identifier")
    public Employee findByIdentifier(@Param("identifier") String identifier);

    /*
     * Activates/Deactivates an employee by updating their status, 'updatedAt' and 'dateOfExit' fields.
     * - Updates the Employee entity for the specified employeeId.
     */
    @Modifying
    @Transactional
    @Query("UPDATE Employee SET updatedAt = :timeStamp, dateOfExit = :timeStamp, status = :status WHERE employeeId = :employeeId")
    public void updateEmployeeStatus(@Param("employeeId") String employeeId, @Param("timeStamp") LocalDateTime timeStamp, @Param("status") UserStatus status);
}