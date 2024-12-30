package com.srinivasa.refrigerationworks.srw.repository;

import com.srinivasa.refrigerationworks.srw.entity.Complaint;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.ComplaintState;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/*
 * Repository for Complaint entity
 */
@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    /*
     * Retrieves complaints by bookedById
     */
    public List<Complaint> findByBookedById(String userId);

    /*
     * Finds complaint by complaintId
     */
    public Complaint findByComplaintId(String complaintId);

    /*
     * Retrieves complaints by state
     */
    public List<Complaint> findByState(ComplaintState state);

    /*
     * Updates complaint state and 'updatedAt'
     */
    @Modifying
    @Transactional
    @Query("UPDATE Complaint SET updatedAt = :updatedAt, state = :state WHERE complaintId = :complaintId")
    public void updateComplaintState(@Param("complaintId") String complaintId, @Param("updatedAt") LocalDateTime updatedAt, @Param("state") ComplaintState state);

    /*
     * Retrieves complaints by technicianId
     */
    public List<Complaint> findByTechnicianId(String userId);
}