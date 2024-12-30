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
import java.util.List;

/*
 * Repository for Employee entity
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /*
     * Finds employee by ID, phone, email, or national ID
     */
    @Query("SELECT e FROM Employee e WHERE e.employeeId = :identifier OR e.phoneNumber = :identifier OR e.email = :identifier or e.nationalIdNumber = :identifier")
    public Employee findByIdentifier(@Param("identifier") String identifier);

    /*
     * Retrieves employees by status
     */
    public List<Employee> findByStatus(UserStatus status);

    /*
     * Updates employee status, 'updatedAt', and 'dateOfExit'
     */
    @Modifying
    @Transactional
    @Query("UPDATE Employee SET updatedAt = :updatedAt, dateOfExit = :dateOfExit, status = :status WHERE employeeId = :employeeId")
    public void updateEmployeeStatus(@Param("employeeId") String employeeId, @Param("updatedAt") LocalDateTime updatedAt, @Param("dateOfExit") LocalDateTime dateOfExit, @Param("status") UserStatus status);
}