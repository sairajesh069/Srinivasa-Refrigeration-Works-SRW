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
 * Repository interface for Complaint entity
 * Provides CRUD operations using JpaRepository
 */
@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    /*
     * Retrieves a list of complaints based on the user ID of the person who booked the complaint.
     * Returns all complaints associated with the given bookedById (userId).
     */
    public List<Complaint> findAllByBookedById(String userId);

    /*
     * Finds and returns a Complaint entity by its complaintId.
     */
    public Complaint findByComplaintId(String complaintId);

    /*
     * Deactivates a complaint by updating their state and 'updatedAt' fields.
     * - Updates the Complaint entity for the specified complaintId.
     */
    @Modifying
    @Transactional
    @Query("UPDATE Complaint SET updatedAt = :updatedAt, state = :state WHERE complaintId = :complaintId")
    public void deactivateComplaint(@Param("complaintId") String complaintId, @Param("updatedAt") LocalDateTime updatedAt, @Param("state") ComplaintState state);
}