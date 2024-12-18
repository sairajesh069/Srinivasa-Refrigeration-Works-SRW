package com.srinivasa.refrigerationworks.srw.utility.mapper;

import com.srinivasa.refrigerationworks.srw.entity.Owner;
import com.srinivasa.refrigerationworks.srw.payload.dto.OwnerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/*
 * Mapper interface for converting between Owner entity and OwnerDTO
 */
@Mapper(componentModel = "spring")
public interface OwnerMapper {

    /*
     * Converts OwnerDTO to Owner entity
     * (ignores ownerReference, createdAt, updatedAt, ownerId fields)
     */
    @Mapping(target = "ownerReference", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "ownerId", ignore = true)
    Owner toEntity(OwnerDTO ownerDTO);

    /*
     * Converts Owner entity to OwnerDTO
     */
    OwnerDTO toDto(Owner owner);
}