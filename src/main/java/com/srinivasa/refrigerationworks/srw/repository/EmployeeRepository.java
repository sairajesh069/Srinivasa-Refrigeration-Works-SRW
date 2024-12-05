package com.srinivasa.refrigerationworks.srw.repository;

import com.srinivasa.refrigerationworks.srw.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * Repository interface for Employee entity
 * Provides CRUD operations using JpaRepository
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
