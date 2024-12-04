package com.srinivasa.refrigerationworks.srw.service;

import com.srinivasa.refrigerationworks.srw.entity.Owner;
import com.srinivasa.refrigerationworks.srw.payload.dto.OwnerDTO;
import com.srinivasa.refrigerationworks.srw.repository.OwnerRepository;
import com.srinivasa.refrigerationworks.srw.utility.mapper.OwnerMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*
 * Service to handle Owner-related operations.
 */
@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    /*
     * Adds a new owner by converting the OwnerDTO to an Owner entity,
     * formatting the phone number, saving it to the repository,
     * generating an owner ID, and returning it.
     */
    @Transactional
    public String addOwner(OwnerDTO ownerDTO) {
        Owner owner = ownerMapper.toEntity(ownerDTO);
        owner.setPhoneNumber("+91" + owner.getPhoneNumber());
        ownerRepository.save(owner);
        owner.setOwnerId("SRW" + String.format("%03d", owner.getOwnerReference()));
        ownerDTO.setOwnerId(owner.getOwnerId());
        return owner.getOwnerId();
    }
}