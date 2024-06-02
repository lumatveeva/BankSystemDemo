package com.bellintegrator.BankSystemDemo.security;

import com.bellintegrator.BankSystemDemo.model.Customer;
import com.bellintegrator.BankSystemDemo.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j
public class MyCustomerDetailService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Попытка найти пользователя: {}", username);
        Customer currentCustomer =  customerRepository.findCustomerByEmail(username).orElseThrow( () -> new UsernameNotFoundException("User not found"));
        log.info("Пользователь найден: {}", currentCustomer.getEmail());
        return new MyCustomerDetail(currentCustomer);
    }
}
