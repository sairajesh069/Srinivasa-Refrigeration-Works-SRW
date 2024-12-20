package com.srinivasa.refrigerationworks.srw.service;

import com.srinivasa.refrigerationworks.srw.entity.Complaint;
import com.srinivasa.refrigerationworks.srw.payload.dto.ComplaintDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.ComplaintIdentifierDTO;
import com.srinivasa.refrigerationworks.srw.repository.ComplaintRepository;
import com.srinivasa.refrigerationworks.srw.utility.common.PhoneNumberFormatter;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.ComplaintState;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.ComplaintStatus;
import com.srinivasa.refrigerationworks.srw.utility.mapper.ComplaintMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        complaint.setContactNumber(PhoneNumberFormatter.formatPhoneNumber(complaint.getContactNumber()));
        String bookedById = userCredentialService.getUserIdByUsername(username);
        complaint.setBookedById(bookedById);
        complaint.setStatus(ComplaintStatus.OPEN);
        complaint.setState(ComplaintState.ACTIVE);
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
    public List<ComplaintDTO> getComplaintByIdentifier(ComplaintIdentifierDTO complaintIdentifierDTO, String userName, String userRole) {
        String identifier = complaintIdentifierDTO.getIdentifier();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate registeredDate = complaintIdentifierDTO.getRegisteredDate();
        String registeredDateFormatted = registeredDate!=null ? registeredDate.format(formatter) : null;
        String phoneNumberFormatted = identifier.matches("\\d{10}") ? PhoneNumberFormatter.formatPhoneNumber(identifier) : identifier;
        List<ComplaintDTO> complaints = !userRole.equals("ROLE_OWNER") ? getComplaintsByUsername(userName)
                : complaintRepository.findAll().stream().map(complaintMapper::toDto).toList();
        return complaints
                .stream()
                .filter(complaint ->
                        complaint.getComplaintId().equals(identifier) ||
                                (complaint.getContactNumber().equals(phoneNumberFormatted) &&
                                        complaint.getCreatedAt().format(formatter).equals(registeredDateFormatted)))
                .toList();
    }

    /*
     * Retrieves the ComplaintDTO by complaintId.
     * - Checks if the user is an owner. If not, ensures the user has access to the complaint.
     * - If the complaint is not booked by the current user (for non-owners), returns null.
     */
    public ComplaintDTO getComplaintById(String complaintId, boolean isOwner, String username) {
        Complaint complaint = complaintRepository.findByComplaintId(complaintId);
        if(!isOwner) {
            String userId = userCredentialService.getUserIdByUsername(username);
            if(!complaint.getBookedById().equals(userId)) {
                return null;
            }
        }
        return complaintMapper.toDto(complaint);
    }

    /*
     * Updates the complaint details by comparing initial and updated ComplaintDTOs.
     * Sets the relevant values and saves the updated complaint to the repository.
     */
    public void updateComplaint(ComplaintDTO initialComplaintDTO, ComplaintDTO updatedComplaintDTO) {
        updatedComplaintDTO.setCreatedAt(initialComplaintDTO.getCreatedAt());
        updatedComplaintDTO.setBookedById(initialComplaintDTO.getBookedById());
        updatedComplaintDTO.setUpdatedAt(initialComplaintDTO.getUpdatedAt());
        updatedComplaintDTO.setClosedAt(initialComplaintDTO.getClosedAt());

        updatedComplaintDTO.setState(updatedComplaintDTO.getState() == null
                ? initialComplaintDTO.getState() : updatedComplaintDTO.getState());

        updatedComplaintDTO.setStatus(updatedComplaintDTO.getStatus() == null
                ? initialComplaintDTO.getStatus() : updatedComplaintDTO.getStatus());

        updatedComplaintDTO.setTechnicianId(updatedComplaintDTO.getTechnicianId() == null
                ? initialComplaintDTO.getTechnicianId() : updatedComplaintDTO.getTechnicianId());

        updatedComplaintDTO.setCustomerFeedback(updatedComplaintDTO.getCustomerFeedback() == null
                ? initialComplaintDTO.getCustomerFeedback() : updatedComplaintDTO.getCustomerFeedback());

        if(!initialComplaintDTO.equals(updatedComplaintDTO)) {
            Complaint complaint = complaintMapper.toEntity(updatedComplaintDTO);
            complaint.setComplaintId(updatedComplaintDTO.getComplaintId());
            complaint.setComplaintReference(Long.parseLong(complaint.getComplaintId().substring(4,12)));
            complaint.setBookedById(updatedComplaintDTO.getBookedById());
            complaint.setUpdatedAt(LocalDateTime.now());
            if(updatedComplaintDTO.getStatus().equals(ComplaintStatus.RESOLVED) && updatedComplaintDTO.getClosedAt() == null) {
                complaint.setClosedAt(LocalDateTime.now());
            }
            complaintRepository.save(complaint);
        }
    }
}