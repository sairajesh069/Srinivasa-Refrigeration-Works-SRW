package com.srinivasa.refrigerationworks.srw.service;

import com.srinivasa.refrigerationworks.srw.entity.Owner;
import com.srinivasa.refrigerationworks.srw.payload.dto.OwnerDTO;
import com.srinivasa.refrigerationworks.srw.repository.OwnerRepository;
import com.srinivasa.refrigerationworks.srw.utility.common.PhoneNumberFormatter;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.UserStatus;
import com.srinivasa.refrigerationworks.srw.utility.mapper.OwnerMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    @CacheEvict(cacheNames = "owners", allEntries = true)
    public String addOwner(OwnerDTO ownerDTO) {
        Owner owner = ownerMapper.toEntity(ownerDTO);
        owner.setPhoneNumber(PhoneNumberFormatter.formatPhoneNumber(owner.getPhoneNumber()));
        owner.setStatus(UserStatus.ACTIVE);
        ownerRepository.save(owner);
        owner.setOwnerId("SRW" + String.format("%03d", owner.getOwnerReference()));
        ownerDTO.setOwnerId(owner.getOwnerId());
        return owner.getOwnerId();
    }

    /*
     * Retrieves a list of all owners from the repository and maps them to OwnerDTO objects.
     * Returns a list of OwnerDTO to be used in other services or controllers.
     */
    @Cacheable(value = "owners", key = "'owner_list'")
    public List<OwnerDTO> getOwnerList() {
        List<Owner> owners = ownerRepository.findAll();
        return owners
                .stream()
                .map(ownerMapper::toDto)
                .toList();
    }

    /*
     * Retrieves a list of all active owners from the repository and maps them to OwnerDTO objects.
     * Returns a list of active OwnerDTO to be used in other services or controllers.
     */
    @Cacheable(value = "owners", key = "'active_owner_list'")
    public List<OwnerDTO> getActiveOwnerList() {
        List<Owner> owners = ownerRepository.findAll();
        return owners
                .stream()
                .filter(owner -> owner.getStatus().name().equals("ACTIVE"))
                .map(ownerMapper::toDto)
                .toList();
    }

    /*
     * Retrieves the owner details based on the provided identifier (phone number, email, or owner ID).
     * If the identifier is a 10-digit phone number, it prefixes it with "+91".
     * Converts the owner entity to a DTO before returning.
     */
    @Cacheable(value = "owner" , key = "'fetch-' + #identifier")
    public OwnerDTO getOwnerByIdentifier(String identifier) {
        identifier = identifier.matches("\\d{10}") ? PhoneNumberFormatter.formatPhoneNumber(identifier) : identifier;
        Owner owner = ownerRepository.findByIdentifier(identifier);
        return ownerMapper.toDto(owner);
    }

    /*
     * Updates owner details by mapping DTO to entity, formatting phone number,
     * and setting updated timestamp before saving to the repository.
     */
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "owners", allEntries = true),
                    @CacheEvict(cacheNames = "owner", key = "'fetch-' + #ownerDTO.ownerId")},
            put = @CachePut(value = "owner", key = "'update-' + #ownerDTO.ownerId"))
    public void updateOwner(OwnerDTO ownerDTO) {
        Owner owner = ownerMapper.toEntity(ownerDTO);
        owner.setOwnerId(ownerDTO.getOwnerId());
        owner.setOwnerReference(Long.parseLong(ownerDTO.getOwnerId().substring(3,6)));
        String updatedPhoneNumber = PhoneNumberFormatter.formatPhoneNumber(owner.getPhoneNumber());
        owner.setPhoneNumber(updatedPhoneNumber);
        owner.setUpdatedAt(LocalDateTime.now());
        ownerRepository.save(owner);
    }

    /*
     * Activates an owner by updating their status to active.
     * - Sets the status to ACTIVE and updates the timestamp.
     */
    @Caching(
            evict = @CacheEvict(cacheNames = "owners", allEntries = true),
            put = @CachePut(value = "owner", key = "'activate-' + #ownerId"))
    public void activateOwner(String ownerId) {
        ownerRepository.updateOwnerStatus(ownerId, LocalDateTime.now(), UserStatus.ACTIVE);
    }

    /*
     * Deactivates an owner by updating their status to inactive.
     * - Sets the status to IN_ACTIVE and updates the timestamp.
     */
    @Caching(
            evict = @CacheEvict(cacheNames = "owners", allEntries = true),
            put = @CachePut(value = "owner", key = "'deactivate-' + #ownerId"))
    public void deactivateOwner(String ownerId) {
        ownerRepository.updateOwnerStatus(ownerId, LocalDateTime.now(), UserStatus.IN_ACTIVE);
    }
}