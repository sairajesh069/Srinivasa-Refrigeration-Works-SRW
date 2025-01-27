package com.srinivasa.refrigerationworks.srw.utility.mapper;

import com.srinivasa.refrigerationworks.srw.entity.Complaint;
import com.srinivasa.refrigerationworks.srw.payload.dto.ComplaintDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/*
 * Mapper interface for converting between Complaint entity and ComplaintDTO
 */
@Mapper(componentModel = "spring")
public interface ComplaintMapper {

    /*
     * Converts ComplaintDTO to Complaint entity
     * (ignores complaintReference, complaintId, bookedById, createdAt and updatedAt fields)
     */
    @Mapping(target = "complaintReference", ignore = true)
    @Mapping(target = "complaintId", ignore = true)
    @Mapping(target = "bookedById", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Complaint toEntity(ComplaintDTO complaintDTO);

    /*
     * Converts Complaint entity to ComplaintDTO
     */
    ComplaintDTO toDto(Complaint complaint);
}