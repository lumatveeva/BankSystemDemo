package com.bellintegrator.BankSystemDemo.service;

import com.bellintegrator.BankSystemDemo.dto.CustomerDTO;
import com.bellintegrator.BankSystemDemo.exceptions.CustomerNotFoundException;
import com.bellintegrator.BankSystemDemo.mappers.CustomerMapper;
import com.bellintegrator.BankSystemDemo.model.Customer;
import com.bellintegrator.BankSystemDemo.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;


    public List<CustomerDTO> findAllCustomerForm() {
        return customerMapper.toListCustomerForm(customerRepository.findAll());
    }

    public CustomerDTO findCustomerFormByCustomerId(UUID id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return customerMapper.toCustomerForm(customer);
    }

    @Transactional
    public void updateCustomer(UUID id, CustomerDTO updatedCustomerDTO) {
        // Найти существующего пользователя по ID
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Optional<Customer> customerByEmail = customerRepository.findCustomerByEmail(existingCustomer.getEmail());
        if (customerByEmail.isPresent() && !customerByEmail.get().getId().equals(id)) {
            throw new IllegalArgumentException("Пользователь с данным email уже существует");
        }
        // Обновить поля пользователя новыми данными
        existingCustomer.setEmail(updatedCustomerDTO.getEmail());
        existingCustomer.setFirstName(updatedCustomerDTO.getFirstName());
        existingCustomer.setLastName(updatedCustomerDTO.getLastName());
        existingCustomer.setPassport(updatedCustomerDTO.getPassport());
        // Если необходимо обновить пароль, кодируйте его
        if (updatedCustomerDTO.getPassword() != null && !updatedCustomerDTO.getPassword().isEmpty()) {
            existingCustomer.setPassword(passwordEncoder.encode(updatedCustomerDTO.getPassword()));
        }

        // Сохранить обновленного пользователя в базе данных
        customerRepository.save(existingCustomer);
    }
}
