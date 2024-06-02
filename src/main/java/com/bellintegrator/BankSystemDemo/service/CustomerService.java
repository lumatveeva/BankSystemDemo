package com.bellintegrator.BankSystemDemo.service;

import com.bellintegrator.BankSystemDemo.exceptions.CustomerNotFoundException;
import com.bellintegrator.BankSystemDemo.model.Customer;
import com.bellintegrator.BankSystemDemo.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomerService {

    private CustomerRepository customerRepository;

    public List<Customer> findAllCustomer(){
        return customerRepository.findAll();
    }
    public Customer findById(UUID id){ return customerRepository.findById(id).orElseThrow(()-> new CustomerNotFoundException("Customer not found"));
    }

    public void saveCustomer(Customer customer){
        customerRepository.save(customer);
    }
    public void updateCustomer(UUID id, Customer updatedCustomer){
        updatedCustomer.setId(id);
        customerRepository.save(updatedCustomer);
    }
}
