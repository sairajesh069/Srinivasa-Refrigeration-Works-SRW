package com.srinivasa.refrigerationworks.srw.service;

import com.srinivasa.refrigerationworks.srw.entity.Owner;
import com.srinivasa.refrigerationworks.srw.payload.dto.OwnerDTO;
import com.srinivasa.refrigerationworks.srw.repository.OwnerRepository;
import com.srinivasa.refrigerationworks.srw.utility.mapper.OwnerMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /*
     * Retrieves a list of all owners from the repository and maps them to OwnerDTO objects.
     * Returns a list of OwnerDTO to be used in other services or controllers.
     */
    public List<OwnerDTO> getOwnerList() {
        List<Owner> owners = ownerRepository.findAll();
        return owners
                .stream()
                .map(ownerMapper::toDto)
                .toList();
    }

    /*
     * Retrieves the owner details based on the provided identifier (phone number, email, or owner ID).
     * If the identifier is a 10-digit phone number, it prefixes it with "+91".
     * Converts the owner entity to a DTO before returning.
     */
    public OwnerDTO getOwnerByIdentifier(String identifier) {
        if(identifier.matches("\\d{10}")) {
            identifier = "+91" + identifier;
        }
        Owner owner = ownerRepository.findByIdentifier(identifier);
        return ownerMapper.toDto(owner);
    }
}