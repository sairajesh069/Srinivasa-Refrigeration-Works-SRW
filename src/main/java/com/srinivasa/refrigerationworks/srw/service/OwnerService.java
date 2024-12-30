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

    /*
     * OwnerRepository for handling owner-related data operations.
     */
    private final OwnerRepository ownerRepository;

    /*
     * OwnerMapper for mapping OwnerDTO to Owner entity and vice versa.
     */
    private final OwnerMapper ownerMapper;

    /*
     * Adds a new owner, formats phone number, saves it, and generates an owner ID.
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
     * Retrieves a list of all owners, mapped to OwnerDTO objects.
     */
    @Cacheable(value = "owners", key = "'owner_list'")
    public List<OwnerDTO> getOwnerList() {
        return ownerRepository
                .findAll()
                .stream()
                .map(ownerMapper::toDto)
                .toList();
    }

    /*
     * Retrieves a list of active owners, mapped to OwnerDTO objects.
     */
    @Cacheable(value = "owners", key = "'active_owner_list'")
    public List<OwnerDTO> getActiveOwnerList() {
        return ownerRepository
                .findByStatus(UserStatus.ACTIVE)
                .stream()
                .map(ownerMapper::toDto)
                .toList();
    }

    /*
     * Retrieves owner details by identifier (phone number, email, or owner ID).
     * Formats phone number if it's a 10-digit number.
     */
    @Cacheable(value = "owner", key = "'fetch-' + #identifier")
    public OwnerDTO getOwnerByIdentifier(String identifier) {
        Owner owner = ownerRepository.findByIdentifier(
                identifier.matches("\\d{10}") ? PhoneNumberFormatter.formatPhoneNumber(identifier) : identifier);
        return ownerMapper.toDto(owner);
    }

    /*
     * Updates owner details, formats phone number, and sets updated timestamp.
     */
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "owners", allEntries = true),
                    @CacheEvict(cacheNames = "owner", key = "'fetch-' + #ownerDTO.ownerId")
            },
            put = @CachePut(value = "owner", key = "'update-' + #ownerDTO.ownerId")
    )
    public void updateOwner(OwnerDTO ownerDTO) {
        Owner owner = ownerMapper.toEntity(ownerDTO);
        owner.setOwnerId(ownerDTO.getOwnerId());
        owner.setOwnerReference(Long.parseLong(ownerDTO.getOwnerId().substring(3, 6)));
        owner.setPhoneNumber(PhoneNumberFormatter.formatPhoneNumber(owner.getPhoneNumber()));
        owner.setUpdatedAt(LocalDateTime.now());
        ownerRepository.save(owner);
    }

    /*
     * Activates an owner by updating their status to active and setting timestamp.
     */
    @Caching(
            evict = @CacheEvict(cacheNames = "owners", allEntries = true),
            put = @CachePut(value = "owner", key = "'activate-' + #ownerId")
    )
    public void activateOwner(String ownerId) {
        ownerRepository.updateOwnerStatus(ownerId, LocalDateTime.now(), UserStatus.ACTIVE);
    }

    /*
     * Deactivates an owner by updating their status to inactive and setting timestamp.
     */
    @Caching(
            evict = @CacheEvict(cacheNames = "owners", allEntries = true),
            put = @CachePut(value = "owner", key = "'deactivate-' + #ownerId")
    )
    public void deactivateOwner(String ownerId) {
        ownerRepository.updateOwnerStatus(ownerId, LocalDateTime.now(), UserStatus.IN_ACTIVE);
    }
}