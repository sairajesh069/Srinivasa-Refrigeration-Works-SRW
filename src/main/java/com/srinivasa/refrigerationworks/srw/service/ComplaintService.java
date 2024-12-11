package com.srinivasa.refrigerationworks.srw.service;

import com.srinivasa.refrigerationworks.srw.entity.Complaint;
import com.srinivasa.refrigerationworks.srw.payload.dto.ComplaintDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.ComplaintIdentifierDTO;
import com.srinivasa.refrigerationworks.srw.repository.ComplaintRepository;
import com.srinivasa.refrigerationworks.srw.utility.mapper.ComplaintMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/*
 * Service layer for handling complaint-related operations.
 * Manages the business logic for registering complaints.
 */
@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final ComplaintMapper complaintMapper;
    private final UserCredentialService userCredentialService;

    /*
     * Registers a new complaint in the system.
     * - Maps the ComplaintDTO to a Complaint entity.
     * - Sets additional details such as contact number format and bookedById.
     * - Saves the complaint to the database and generates a unique complaint ID.
     *
     * @param complaintDTO The DTO containing complaint details.
     * @param username The username of the user registering the complaint.
     */
    @Transactional
    public void registerComplaint(ComplaintDTO complaintDTO, String username) {
        Complaint complaint = complaintMapper.toEntity(complaintDTO);
        complaint.setContactNumber("+91" + complaint.getContactNumber());
        String bookedById = userCredentialService.getUserIdByUsername(username);
        complaint.setBookedById(bookedById);
        complaintRepository.save(complaint);
        complaint.setComplaintId("SRWC" + String.format("%08d", complaint.getComplaintReference()));
        complaintDTO.setComplaintId(complaint.getComplaintId());
        complaintDTO.setStatus(complaint.getStatus());
    }

    /*
     * Retrieves a list of complaints based on the username.
     * First, the user ID is fetched using the provided username, and then complaints associated with that user ID are retrieved.
     */
    public List<ComplaintDTO> getComplaintsByUsername(String username) {
        String userId = userCredentialService.getUserIdByUsername(username);
        List<Complaint> complaints = complaintRepository.findAllByBookedById(userId);
        return complaints
                .stream()
                .map(complaintMapper::toDto)
                .toList();
    }

    /*
     * Retrieves all complaints from the repository.
     * Returns the list of complaints to be used in other services or controllers.
     */
    public List<ComplaintDTO> getComplaintList() {
        List<Complaint> complaints =  complaintRepository.findAll();
        return complaints
                .stream()
                .map(complaintMapper::toDto)
                .toList();
    }

    /*
     * Fetches complaints based on the identifier and user role.
     * If the user is not an OWNER, retrieves complaints for the logged-in user.
     * Filters complaints by complaint ID or contact number and registration date.
     */
    public List<ComplaintDTO> getComplaintByIdentifier(ComplaintIdentifierDTO complaintIdentifierDTO, String userName) {
        String identifier = complaintIdentifierDTO.getIdentifier();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate registeredDate = complaintIdentifierDTO.getRegisteredDate();
        String registeredDateFormatted = registeredDate!=null ? registeredDate.format(formatter) : null;
        String role = userCredentialService.getUserTypeByUsername(userName);
        List<ComplaintDTO> complaints = !role.equals("OWNER") ? getComplaintsByUsername(userName)
                : complaintRepository.findAll().stream().map(complaintMapper::toDto).toList();
        return complaints
                .stream()
                .filter(complaint ->
                        complaint.getComplaintId().equals(identifier) ||
                                (complaint.getContactNumber().equals("+91" + identifier) &&
                                        complaint.getCreatedAt().format(formatter).equals(registeredDateFormatted)))
                .toList();
    }
}