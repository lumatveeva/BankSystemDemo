package com.bellintegrator.BankSystemDemo.service;

import com.bellintegrator.BankSystemDemo.dto.CustomerForm;
import com.bellintegrator.BankSystemDemo.exceptions.CustomerNotFoundException;
import com.bellintegrator.BankSystemDemo.mappers.CustomerMapper;
import com.bellintegrator.BankSystemDemo.model.Customer;
import com.bellintegrator.BankSystemDemo.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final RegistrationService registrationService;


    public List<CustomerForm> findAllCustomerForm() {
        return customerMapper.toListCustomerForm(customerRepository.findAll());
    }

    public CustomerForm findCustomerFormByCustomerId(UUID id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return customerMapper.toCustomerForm(customer);
    }

    @Transactional
    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Transactional
    public void updateCustomer(UUID id, CustomerForm updatedCustomerForm) {
        Customer updatedCustomer = customerMapper.toCustomer(updatedCustomerForm);
        updatedCustomer.setId(id);
        registrationService.register(updatedCustomer);
        customerRepository.save(updatedCustomer);
    }
}
