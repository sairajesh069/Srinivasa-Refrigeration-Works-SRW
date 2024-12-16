package com.srinivasa.refrigerationworks.srw.repository;

import com.srinivasa.refrigerationworks.srw.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}