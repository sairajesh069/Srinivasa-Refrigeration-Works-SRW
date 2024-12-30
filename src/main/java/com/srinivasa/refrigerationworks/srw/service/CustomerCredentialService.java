package com.srinivasa.refrigerationworks.srw.service;

import com.srinivasa.refrigerationworks.srw.entity.UserCredential;
import com.srinivasa.refrigerationworks.srw.payload.dto.CustomerCredentialDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.CustomerDTO;
import com.srinivasa.refrigerationworks.srw.utility.common.PhoneNumberFormatter;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.UserType;
import com.srinivasa.refrigerationworks.srw.utility.mapper.UserCredentialMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*
 * Service to handle CustomerCredential-related operations.
 */
@Service
@RequiredArgsConstructor
public class CustomerCredentialService {

    /*
     * Service for user credential operations.
     */
    private final UserCredentialService userCredentialService;

    /*
     * Mapper for converting user credential DTO to entity.
     */
    private final UserCredentialMapper userCredentialMapper;

    /*
     * Service for customer operations.
     */
    private final CustomerService customerService;

    /*
     * Adds customer credential and saves it with role "ROLE_CUSTOMER".
     */
    public void addCustomerCredential(CustomerCredentialDTO customerCredentialDTO) {
        String customerId = customerService.addCustomer(customerCredentialDTO.getCustomerDTO());
        UserCredential userCredential = userCredentialMapper.toEntity(customerCredentialDTO.getUserCredentialDTO());
        userCredential.setUserId(customerId);
        userCredential.setPhoneNumber(customerCredentialDTO.getCustomerDTO().getPhoneNumber());
        userCredentialService.saveCredential(userCredential, UserType.CUSTOMER, "ROLE_CUSTOMER");
    }

    /*
     * Updates customer and phone number if changes are detected.
     */
    @Transactional
    public void updateCustomer(CustomerDTO initialCustomerDTO, CustomerDTO updatedCustomerDTO) {
        updatedCustomerDTO.setStatus(updatedCustomerDTO.getStatus() == null ? initialCustomerDTO.getStatus() : updatedCustomerDTO.getStatus());
        if (!initialCustomerDTO.equals(updatedCustomerDTO)) {
            customerService.updateCustomer(updatedCustomerDTO);
            String initialPhoneNumber = PhoneNumberFormatter.formatPhoneNumber(initialCustomerDTO.getPhoneNumber());
            String updatedPhoneNumber = PhoneNumberFormatter.formatPhoneNumber(updatedCustomerDTO.getPhoneNumber());
            if (!updatedPhoneNumber.equals(initialPhoneNumber)) {
                userCredentialService.updateUserPhoneNumber(updatedCustomerDTO.getCustomerId(), updatedPhoneNumber);
            }
        }
    }

    /*
     * Activates customer and their credentials.
     */
    @Transactional
    public void activateCustomer(String customerId) {
        customerService.activateCustomer(customerId);
        userCredentialService.updateUserStatus(customerId, (byte) 1);
    }

    /*
     * Deactivates customer and their credentials.
     */
    @Transactional
    public void deactivateCustomer(String customerId) {
        customerService.deactivateCustomer(customerId);
        userCredentialService.updateUserStatus(customerId, (byte) 0);
    }

    /*
     * Retrieves CustomerDTO by customerId.
     */
    public CustomerDTO getCustomerById(String customerId) {
        return customerService.getCustomerByIdentifier(customerId);
    }
}