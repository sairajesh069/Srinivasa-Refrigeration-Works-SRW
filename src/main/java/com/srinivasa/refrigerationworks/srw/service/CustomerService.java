package com.srinivasa.refrigerationworks.srw.service;

import com.srinivasa.refrigerationworks.srw.entity.Customer;
import com.srinivasa.refrigerationworks.srw.payload.dto.CustomerDTO;
import com.srinivasa.refrigerationworks.srw.repository.CustomerRepository;
import com.srinivasa.refrigerationworks.srw.utility.mapper.CustomerMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public String addCustomer(CustomerDTO customerDTO) {
        Customer customer = customerMapper.toEntity(customerDTO);
        customer.setPhoneNumber("+91" + customer.getPhoneNumber());
        customerRepository.save(customer);
        customer.setCustomerId("SRW" + String.format("%07d", customer.getCustomerReference()));
        customerDTO.setCustomerId(customer.getCustomerId());
        return customer.getCustomerId();
    }

    /*
     * Retrieves a list of all customers from the repository and maps them to CustomerDTO objects.
     * Returns a list of CustomerDTO to be used in other services or controllers.
     */
    public List<CustomerDTO> getCustomerList() {
        List<Customer> customers = customerRepository.findAll();
        return customers
                .stream()
                .map(customerMapper::toDto)
                .toList();
    }
}
