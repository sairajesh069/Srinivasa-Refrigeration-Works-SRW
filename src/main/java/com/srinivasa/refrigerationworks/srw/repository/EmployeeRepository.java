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
     * Checks if an employee with the given phone number exists in the database.
     */
    @Query("SELECT COUNT(e)>0 FROM Employee e WHERE e.phoneNumber = :phoneNumber")
    public boolean existsByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    /*
     * Checks if an employee with the given email exists in the database.
     */
    @Query("SELECT COUNT(e)>0 FROM Employee e WHERE e.email = :email")
    public boolean existsByEmail(@Param("email") String email);
}
