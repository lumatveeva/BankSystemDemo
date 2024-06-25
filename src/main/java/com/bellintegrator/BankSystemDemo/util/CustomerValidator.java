package com.bellintegrator.BankSystemDemo.util;

import com.bellintegrator.BankSystemDemo.dto.CustomerDTO;
import com.bellintegrator.BankSystemDemo.mappers.CustomerMapper;
import com.bellintegrator.BankSystemDemo.model.Customer;
import com.bellintegrator.BankSystemDemo.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@AllArgsConstructor
public class CustomerValidator implements Validator {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public boolean supports(Class<?> clazz) {
        return Customer.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CustomerDTO currentCustomerDTO = (CustomerDTO) target;
        Customer currentCustomer = customerMapper.toCustomer(currentCustomerDTO);
        Optional<Customer> customerByEmail = customerRepository.findCustomerByEmail(currentCustomer.getEmail());
        if (customerByEmail.isPresent()) {
            errors.rejectValue("email", "", "Пользователь с данным email уже существует");
        }
    }
}
