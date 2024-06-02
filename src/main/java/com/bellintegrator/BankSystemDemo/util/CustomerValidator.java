package com.bellintegrator.BankSystemDemo.util;

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

    @Override
    public boolean supports(Class<?> clazz) {
        return Customer.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Customer currentCustomer = (Customer) target;
        Optional<Customer> customerByEmail = customerRepository.findCustomerByEmail(currentCustomer.getEmail());
        if(customerByEmail.isPresent()){
            errors.rejectValue("email", "", "User already exist");
        }
    }
}
