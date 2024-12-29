package com.srinivasa.refrigerationworks.srw.service;

import com.srinivasa.refrigerationworks.srw.entity.Customer;
import com.srinivasa.refrigerationworks.srw.payload.dto.CustomerDTO;
import com.srinivasa.refrigerationworks.srw.repository.CustomerRepository;
import com.srinivasa.refrigerationworks.srw.utility.common.PhoneNumberFormatter;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.UserStatus;
import com.srinivasa.refrigerationworks.srw.utility.mapper.CustomerMapper;
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
 * Service to handle Customer-related operations.
 */
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    /*
     * Adds a new customer by converting the CustomerDTO to a Customer entity,
     * formatting the phone number, saving it to the repository,
     * generating a customer ID, and returning it.
     */
    @Transactional
    @CacheEvict(cacheNames = "customers", allEntries = true)
    public String addCustomer(CustomerDTO customerDTO) {
        Customer customer = customerMapper.toEntity(customerDTO);
        customer.setPhoneNumber(PhoneNumberFormatter.formatPhoneNumber(customer.getPhoneNumber()));
        customer.setStatus(UserStatus.ACTIVE);
        customerRepository.save(customer);
        customer.setCustomerId("SRW" + String.format("%07d", customer.getCustomerReference()));
        customerDTO.setCustomerId(customer.getCustomerId());
        return customer.getCustomerId();
    }

    /*
     * Retrieves a list of all customers from the repository and maps them to CustomerDTO objects.
     * Returns a list of CustomerDTO to be used in other services or controllers.
     */
    @Cacheable(value = "customers", key = "'customer_list'")
    public List<CustomerDTO> getCustomerList() {
        return customerRepository
                .findAll()
                .stream()
                .map(customerMapper::toDto)
                .toList();
    }

    /*
     * Retrieves a list of all active customers from the repository and maps them to CustomerDTO objects.
     * Returns a list of active CustomerDTO to be used in other services or controllers.
     */
    @Cacheable(value = "customers", key = "'active_customer_list'")
    public List<CustomerDTO> getActiveCustomerList() {
        return customerRepository
                .findByStatus(UserStatus.ACTIVE)
                .stream()
                .map(customerMapper::toDto)
                .toList();
    }

    /*
     * Retrieves the customer details based on the provided identifier (phone number, email, or customer ID).
     * If the identifier is a 10-digit phone number, it prefixes it with "+91".
     * Converts the customer entity to a DTO before returning.
     */
    @Cacheable(value = "customer", key = "'fetch-' + #identifier")
    public CustomerDTO getCustomerByIdentifier(String identifier) {
        Customer customer = customerRepository.findByIdentifier(
                identifier.matches("\\d{10}") ? PhoneNumberFormatter.formatPhoneNumber(identifier) : identifier);
        return customerMapper.toDto(customer);
    }

    /*
     * Updates customer details by mapping DTO to entity, formatting phone number,
     * and setting updated timestamp before saving to the repository.
     */
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "customers", allEntries = true),
                    @CacheEvict(cacheNames = "customer", key = "'fetch-' + #customerDTO.customerId")
            },
            put = @CachePut(value = "customer", key = "'update-' + #customerDTO.customerId")
    )
    public void updateCustomer(CustomerDTO customerDTO) {
        Customer customer = customerMapper.toEntity(customerDTO);
        customer.setCustomerId(customerDTO.getCustomerId());
        customer.setCustomerReference(Long.parseLong(customerDTO.getCustomerId().substring(3,10)));
        customer.setPhoneNumber(PhoneNumberFormatter.formatPhoneNumber(customer.getPhoneNumber()));
        customer.setUpdatedAt(LocalDateTime.now());
        customerRepository.save(customer);
    }

    /*
     * Activates a customer by updating their status to active.
     * - Sets the status to ACTIVE and updates the timestamp.
     */
    @Caching(
            evict = @CacheEvict(cacheNames = "customers", allEntries = true),
            put = @CachePut(value = "customer", key = "'activate-' + #customerId")
    )
    public void activateCustomer(String customerId) {
        customerRepository.updateCustomerStatus(customerId, LocalDateTime.now(), UserStatus.ACTIVE);
    }

    /*
     * Deactivates a customer by updating their status to inactive.
     * - Sets the status to IN_ACTIVE and updates the timestamp.
     */
    @Caching(
            evict = @CacheEvict(cacheNames = "customers", allEntries = true),
            put = @CachePut(value = "customer", key = "'deactivate-' + #customerId")
    )
    public void deactivateCustomer(String customerId) {
        customerRepository.updateCustomerStatus(customerId, LocalDateTime.now(), UserStatus.IN_ACTIVE);
    }
}