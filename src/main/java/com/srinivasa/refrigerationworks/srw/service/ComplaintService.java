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
 * Service for complaint-related operations.
 */
@Service
@RequiredArgsConstructor
public class ComplaintService {

    /*
     * Repository for complaint data.
     */
    private final ComplaintRepository complaintRepository;

    /*
     * Mapper for converting complaint entities to DTOs.
     */
    private final ComplaintMapper complaintMapper;

    /*
     * Service for employee-related operations.
     */
    private final EmployeeService employeeService;

    /*
     * Registers a new complaint and sets initial values.
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
     * Retrieves complaints for a specific user based on bookedById.
     */
    @Cacheable(value = "complaints", key = "'my_complaint_list-' + #bookedById")
    public List<ComplaintDTO> getComplaintsByBookedById(String bookedById) {
        return complaintRepository
                .findByBookedById(bookedById)
                .stream()
                .map(complaintMapper::toDto)
                .toList();
    }

    /*
     * Retrieves all complaints.
     */
    @Cacheable(value = "complaints", key = "'complaint_list'")
    public List<ComplaintDTO> getComplaintList() {
        return complaintRepository
                .findAll()
                .stream()
                .map(complaintMapper::toDto)
                .toList();
    }

    /*
     * Retrieves active complaints.
     */
    @Cacheable(value = "complaints", key = "'active_complaint_list'")
    public List<ComplaintDTO> getActiveComplaintList() {
        return complaintRepository
                .findByState(ComplaintState.ACTIVE)
                .stream()
                .map(complaintMapper::toDto)
                .toList();
    }

    /*
     * Retrieves complaints by identifier, filtered by user role and date.
     */
    @Cacheable(value = "complaint", key = "'fetchBy-' + #complaintIdentifierDTO.identifier")
    public List<ComplaintDTO> getComplaintByIdentifier(ComplaintIdentifierDTO complaintIdentifierDTO, String bookedById, String userRole) {
        String identifier = complaintIdentifierDTO.getIdentifier();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate registeredDate = complaintIdentifierDTO.getRegisteredDate();
        String registeredDateFormatted = registeredDate != null ? registeredDate.format(formatter) : null;
        String phoneNumberFormatted = (identifier != null && identifier.matches("\\d{10}"))
                ? PhoneNumberFormatter.formatPhoneNumber(identifier) : identifier;
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
     * Checks if a user can access a complaint based on user role and complaint details.
     */
    @Cacheable(value = "complaint", key = "'complaint_access_check-' + #complaintId + '&' + #userId")
    public boolean canUserAccess(String complaintId, boolean isOwner, String userId) {
        if (isOwner) {
            return true;
        } else {
            Complaint complaint = complaintRepository.findByComplaintId(complaintId);
            return complaint.getBookedById().equals(userId) || complaint.getTechnicianId().equals(userId);
        }
    }

    /*
     * Retrieves complaint details by complaintId.
     */
    @Cacheable(value = "complaint", key = "'fetch-' + #complaintId")
    public ComplaintDTO getComplaintById(String complaintId) {
        return complaintMapper
                .toDto(complaintRepository
                        .findByComplaintId(complaintId));
    }

    /*
     * Updates complaint details and evicts relevant cache entries.
     */
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "complaints", allEntries = true),
                    @CacheEvict(cacheNames = "complaint", key = "'fetch-' + #updatedComplaintDTO.complaintId")
            },
            put = @CachePut(value = "complaint", key = "'update-' + #updatedComplaintDTO.complaintId")
    )
    public void updateComplaint(ComplaintDTO initialComplaintDTO, ComplaintDTO updatedComplaintDTO) {
        updatedComplaintDTO.setCreatedAt(initialComplaintDTO.getCreatedAt());
        updatedComplaintDTO.setBookedById(initialComplaintDTO.getBookedById());
        updatedComplaintDTO.setUpdatedAt(initialComplaintDTO.getUpdatedAt());
        updatedComplaintDTO.setClosedAt(initialComplaintDTO.getClosedAt());
        updatedComplaintDTO.setState(initialComplaintDTO.getState());
        if (!initialComplaintDTO.equals(updatedComplaintDTO)) {
            Complaint complaint = complaintMapper.toEntity(updatedComplaintDTO);
            complaint.setComplaintId(updatedComplaintDTO.getComplaintId());
            complaint.setComplaintReference(Long.parseLong(complaint.getComplaintId().substring(4, 12)));
            complaint.setBookedById(updatedComplaintDTO.getBookedById());
            complaint.setUpdatedAt(LocalDateTime.now());
            if (updatedComplaintDTO.getStatus().equals(ComplaintStatus.RESOLVED) && updatedComplaintDTO.getClosedAt() == null) {
                complaint.setClosedAt(LocalDateTime.now());
            }
            complaintRepository.save(complaint);
        }
    }

    /*
     * Activates a complaint and updates its state to active.
     */
    @Caching(
            evict = @CacheEvict(cacheNames = "complaints", allEntries = true),
            put = @CachePut(value = "complaint", key = "'activate-' + #complaintId")
    )
    public void activateComplaint(String complaintId) {
        complaintRepository.updateComplaintState(complaintId, LocalDateTime.now(), ComplaintState.ACTIVE);
    }

    /*
     * Deactivates a complaint and updates its state to inactive.
     */
    @Caching(
            evict = @CacheEvict(cacheNames = "complaints", allEntries = true),
            put = @CachePut(value = "complaint", key = "'deactivate-' + #complaintId")
    )
    public void deactivateComplaint(String complaintId) {
        complaintRepository.updateComplaintState(complaintId, LocalDateTime.now(), ComplaintState.IN_ACTIVE);
    }

    /*
     * Retrieves complaints assigned to a specific employee (technician).
     */
    @Cacheable(value = "complaints", key = "'fetchByTechnicianId-' + #technicianId")
    public List<ComplaintDTO> getComplaintsByTechnicianId(String technicianId) {
        List<Complaint> complaints = complaintRepository.findByTechnicianId(technicianId);
        return complaints
                .stream()
                .map(complaintMapper::toDto)
                .toList();
    }

    /*
     * Retrieves active employee IDs from the employee service.
     */
    public List<String> getActiveEmployeeIds() {
        return employeeService.getActiveEmployeeIds();
    }
}