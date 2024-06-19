package com.bellintegrator.BankSystemDemo.service;

import com.bellintegrator.BankSystemDemo.dto.CustomerForm;
import com.bellintegrator.BankSystemDemo.exceptions.CustomerNotFoundException;
import com.bellintegrator.BankSystemDemo.mappers.CustomerMapper;
import com.bellintegrator.BankSystemDemo.model.Customer;
import com.bellintegrator.BankSystemDemo.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerMapper customerMapper;


    public List<CustomerForm> findAllCustomerForm() {
        List<Customer> customerList = customerRepository.findAll();
        List<CustomerForm> customerFormList = new ArrayList<>();
        for (Customer customer : customerList) {
            customerFormList.add(customerMapper.toCustomerForm(customer));
        }
        return customerFormList;
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
        customerRepository.save(updatedCustomer);
    }
}
