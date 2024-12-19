package com.srinivasa.refrigerationworks.srw.repository;

import com.srinivasa.refrigerationworks.srw.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}