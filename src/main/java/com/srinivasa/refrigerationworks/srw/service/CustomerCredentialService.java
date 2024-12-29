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

    private final UserCredentialService userCredentialService;
    private final UserCredentialMapper userCredentialMapper;
    private final CustomerService customerService;

    /*
     * Adds user credential for the customer. The customer's details are added,
     * and user credentials are saved with the role "ROLE_CUSTOMER".
     */
    public void addCustomerCredential(CustomerCredentialDTO customerCredentialDTO) {
        String customerId = customerService.addCustomer(customerCredentialDTO.getCustomerDTO());
        UserCredential userCredential = userCredentialMapper.toEntity(customerCredentialDTO.getUserCredentialDTO());
        userCredential.setUserId(customerId);
        userCredential.setPhoneNumber(customerCredentialDTO.getCustomerDTO().getPhoneNumber());
        userCredentialService.saveCredential(userCredential, UserType.CUSTOMER, "ROLE_CUSTOMER");
    }

    /*
     * Updates customer details if changes are detected and synchronizes phone number.
     * - Checks for changes between initial and updated DTOs.
     * - Updates customer and phone number only if modified.
     */
    @Transactional
    public void updateCustomer(CustomerDTO initialCustomerDTO, CustomerDTO updatedCustomerDTO) {
        updatedCustomerDTO.setStatus(updatedCustomerDTO.getStatus()==null ? initialCustomerDTO.getStatus() : updatedCustomerDTO.getStatus());
        if(!initialCustomerDTO.equals(updatedCustomerDTO)) {
            customerService.updateCustomer(updatedCustomerDTO);
            String initialPhoneNumber = PhoneNumberFormatter.formatPhoneNumber(initialCustomerDTO.getPhoneNumber());
            String updatedPhoneNumber = PhoneNumberFormatter.formatPhoneNumber(updatedCustomerDTO.getPhoneNumber());
            if(!updatedPhoneNumber.equals(initialPhoneNumber)) {
                userCredentialService.updateUserPhoneNumber(updatedCustomerDTO.getCustomerId(), updatedPhoneNumber);
            }
        }
    }

    /*
     * Activates a customer and their associated user credentials.
     * - Updates the customer's status to active and timestamps the activation.
     * - Activates the corresponding user credentials.
     */
    @Transactional
    public void activateCustomer(String customerId) {
        customerService.activateCustomer(customerId);
        userCredentialService.updateUserStatus(customerId, (byte) 1);
    }

    /*
     * Deactivates a customer and their associated user credentials.
     * - Updates the customer's status to inactive and timestamps the deactivation.
     * - Deactivates the corresponding user credentials.
     */
    @Transactional
    public void deactivateCustomer(String customerId) {
        customerService.deactivateCustomer(customerId);
        userCredentialService.updateUserStatus(customerId, (byte) 0);
    }

    /*
     * Retrieves the CustomerDTO by customerId.
     */
    public CustomerDTO getCustomerById(String customerId) {
        return customerService.getCustomerByIdentifier(customerId);
    }
}