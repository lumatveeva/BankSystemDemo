package com.bellintegrator.BankSystemDemo.service;

import com.bellintegrator.BankSystemDemo.model.Customer;
import com.bellintegrator.BankSystemDemo.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Log4j2
public class RegistrationService {

    private final CustomerRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void register(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRole("ROLE_USER");
        userRepository.save(customer);
        log.info("Пользователь  {} сохранен в БД", customer.getEmail());
    }
}
