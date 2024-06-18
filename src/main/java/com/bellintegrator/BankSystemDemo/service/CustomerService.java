package com.bellintegrator.BankSystemDemo.service;

import com.bellintegrator.BankSystemDemo.exceptions.CustomerNotFoundException;
import com.bellintegrator.BankSystemDemo.model.Customer;
import com.bellintegrator.BankSystemDemo.repository.CustomerRepository;
import com.bellintegrator.BankSystemDemo.util.CustomerValidator;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;


    public List<Customer> findAllCustomer(){
        return customerRepository.findAll();
    }
    public Customer findById(UUID id){ return customerRepository.findById(id).orElseThrow(()-> new CustomerNotFoundException("Customer not found"));
    }

    @Transactional
    public void saveCustomer(Customer customer){
        customerRepository.save(customer);
    }

    @Transactional
    public void updateCustomer(UUID id, Customer updatedCustomer){
        updatedCustomer.setId(id);
        customerRepository.save(updatedCustomer);
    }
}
