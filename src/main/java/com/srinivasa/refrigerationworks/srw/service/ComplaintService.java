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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
    private final EmployeeService employeeService;

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
    @CacheEvict(cacheNames = "complaints", allEntries = true)
    public void registerComplaint(ComplaintDTO complaintDTO, String bookedById) {
        Complaint complaint = complaintMapper.toEntity(complaintDTO);
        complaint.setContactNumber(PhoneNumberFormatter.formatPhoneNumber(complaint.getContactNumber()));
        complaint.setBookedById(bookedById);
        complaint.setStatus(ComplaintStatus.OPEN);
        complaint.setState(ComplaintState.ACTIVE);
        complaintRepository.save(complaint);
        complaint.setComplaintId("SRWC" + String.format("%08d", complaint.getComplaintReference()));
        complaintDTO.setComplaintId(complaint.getComplaintId());
        complaintDTO.setStatus(complaint.getStatus());
    }

    /*
     * Retrieves a list of complaints associated with the registered user's ID.
     */
    @Cacheable(value = "complaints", key = "'my_complaint_list'")
    public List<ComplaintDTO> getComplaintsByBookedById(String bookedById) {
        List<Complaint> complaints = complaintRepository.findAllByBookedById(bookedById);
        return complaints
                .stream()
                .map(complaintMapper::toDto)
                .toList();
    }

    /*
     * Retrieves all complaints from the repository.
     * Returns the list of complaints to be used in other services or controllers.
     */
    @Cacheable(value = "complaints", key = "'complaint_list'")
    public List<ComplaintDTO> getComplaintList() {
        List<Complaint> complaints =  complaintRepository.findAll();
        return complaints
                .stream()
                .map(complaintMapper::toDto)
                .toList();
    }

    /*
     * Retrieves all active complaints from the repository.
     * Returns the list of active complaints to be used in other services or controllers.
     */
    @Cacheable(value = "complaints", key = "'active_complaint_list'")
    public List<ComplaintDTO> getActiveComplaintList() {
        List<Complaint> complaints =  complaintRepository.findAll();
        return complaints
                .stream()
                .filter(complaint -> complaint.getState().name().equals("ACTIVE"))
                .map(complaintMapper::toDto)
                .toList();
    }

    /*
     * Fetches complaints based on the identifier and user role.
     * If the user is not an OWNER, retrieves complaints for the logged-in user.
     * Filters complaints by complaint ID or contact number and registration date.
     */
    @Cacheable(value = "complaint", key = "'fetchBy-' + #complaintIdentifierDTO.identifier")
    public List<ComplaintDTO> getComplaintByIdentifier(ComplaintIdentifierDTO complaintIdentifierDTO, String bookedById, String userRole) {
        String identifier = complaintIdentifierDTO.getIdentifier();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate registeredDate = complaintIdentifierDTO.getRegisteredDate();
        String registeredDateFormatted = registeredDate!=null ? registeredDate.format(formatter) : null;
        String phoneNumberFormatted = identifier.matches("\\d{10}") ? PhoneNumberFormatter.formatPhoneNumber(identifier) : identifier;
        List<ComplaintDTO> complaints = !userRole.equals("ROLE_OWNER") ? getComplaintsByBookedById(bookedById)
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
    @Cacheable(value = "complaint", key = "'fetchByComplaintId-' + #complaintId")
    public ComplaintDTO getComplaintById(String complaintId, boolean isOwner, String userId) {
        Complaint complaint = complaintRepository.findByComplaintId(complaintId);
        if(!isOwner) {
            if((!complaint.getBookedById().equals(userId) && !complaint.getTechnicianId().equals(userId))) {
                return null;
            }
        }
        return complaintMapper.toDto(complaint);
    }

    /*
     * Updates the complaint details by comparing initial and updated ComplaintDTOs.
     * Sets the relevant values and saves the updated complaint to the repository.
     */
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "complaints", allEntries = true),
                    @CacheEvict(cacheNames = "complaint", key = "'fetch-' + #updatedComplaintDTO.complaintId")},
            put = @CachePut(value = "complaint", key = "'update-' + #updatedComplaintDTO.complaintId"))
    public void updateComplaint(ComplaintDTO initialComplaintDTO, ComplaintDTO updatedComplaintDTO) {
        updatedComplaintDTO.setCreatedAt(initialComplaintDTO.getCreatedAt());
        updatedComplaintDTO.setBookedById(initialComplaintDTO.getBookedById());
        updatedComplaintDTO.setUpdatedAt(initialComplaintDTO.getUpdatedAt());
        updatedComplaintDTO.setClosedAt(initialComplaintDTO.getClosedAt());
        updatedComplaintDTO.setState(initialComplaintDTO.getState());
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

    /*
     * Activates a complaint by updating their state to active.
     * - Sets the state to ACTIVE and updates the timestamp.
     */
    @Caching(
            evict = @CacheEvict(cacheNames = "complaints", allEntries = true),
            put = @CachePut(value = "complaint", key = "'activate-' + #complaintId"))
    public void activateComplaint(String complaintId) {
        complaintRepository.updateComplaintState(complaintId, LocalDateTime.now(), ComplaintState.ACTIVE);
    }

    /*
     * Deactivates a complaint by updating their state to inactive.
     * - Sets the state to IN_ACTIVE and updates the timestamp.
     */
    @Caching(
            evict = @CacheEvict(cacheNames = "complaints", allEntries = true),
            put = @CachePut(value = "complaint", key = "'deactivate-' + #complaintId"))
    public void deactivateComplaint(String complaintId) {
        complaintRepository.updateComplaintState(complaintId, LocalDateTime.now(), ComplaintState.IN_ACTIVE);
    }

    /*
     * Retrieves a list of complaints assigned to an employee.
     */
    @Cacheable(value = "complaints", key = "'fetchByTechnicianId-' + #technicianId")
    public List<ComplaintDTO> getComplaintsByTechnicianId(String technicianId) {
        List<Complaint> complaints = complaintRepository.findAllByTechnicianId(technicianId);
        return complaints
                .stream()
                .map(complaintMapper::toDto)
                .toList();
    }

    /*
     * Fetches the list of active employee IDs from the employee service.
     */
    public List<String> getActiveEmployeeIds() {
        return employeeService.getActiveEmployeeIds();
    }
}