package com.srinivasa.refrigerationworks.srw.utility.mapper;

import com.srinivasa.refrigerationworks.srw.entity.UserCredential;
import com.srinivasa.refrigerationworks.srw.payload.dto.UserCredentialDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/*
 * Mapper interface for converting between UserCredential entity and UserCredentialDTO
 * Uses MapStruct for automatic mapping
 */
@Mapper(componentModel = "spring")
public interface UserCredentialMapper {

    /*
     * Converts UserCredentialDTO to UserCredential entity
     * Ignores userId, phoneNumber, enabled, userType, userRoles fields during mappings
     */
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "userType", ignore = true)
    @Mapping(target = "userRoles", ignore = true)
    UserCredential toEntity(UserCredentialDTO userCredentialDTO);

    /*
     * Converts UserCredential entity to UserCredentialDTO
     */
    UserCredentialDTO toDto(UserCredential userCredential);
}

